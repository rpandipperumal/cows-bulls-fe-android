package com.techtweakz.cowsandbulls;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.StompClient;

public class HostViewActivity extends AppCompatActivity {

    private static final String TAG = "HostViewActivity";

    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;

    private String hostUsername;
    private String roomName;
    private String hostWord;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view);

        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter = new MyPagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            String playerName = pagerAdapter.getPlayerNameAt(position);
            tab.setText(playerName != null ? "Player " + playerName : "Player " + (position + 1));
        }).attach();

        Intent intent = getIntent();
        hostUsername = intent.getStringExtra(HostActivity.PERSON_ID);
        roomName = intent.getStringExtra(HostActivity.ROOM_NAME);
        hostWord = intent.getStringExtra(HostActivity.HOST_WORD);

        mStompClient = StompUtil.createStompClient(hostUsername);
        compositeDisposable = new CompositeDisposable();

        subscribeToPlayerUpdates();
    }

    private void subscribeToPlayerUpdates() {
        String hostTopic = "/topic/" + roomName + "/host";

        Disposable dispTopic = mStompClient.topic(hostTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    try {
                        JSONObject jsonObject = new JSONObject(topicMessage.getPayload());
                        GameData gameData = sendMessageToPlayer(jsonObject);

                        String playerUsername = gameData.getPlayerName();
                        String displayWord = gameData.getWord() + " | " + gameData.getCowsCount() + " | " + gameData.getBullsCount();

                        //  String displayWord = gameData.getWord() + " Cows: " + gameData.getCowsCount() + " Bulls: " + gameData.getBullsCount();
                        pagerAdapter.addOrUpdatePlayerTab(playerUsername, displayWord);

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                    }
                }, throwable -> Log.e(TAG, "Error on subscribe topic", throwable));

        compositeDisposable.add(dispTopic);
    }

    public GameData sendMessageToPlayer(JSONObject jsonObject) throws JSONException {
        ObjectMapper mapper = new ObjectMapper();
        GameData gameData = getGameDataWithCowsBullsCount(jsonObject);

        try {
            String gameDataJson = mapper.writeValueAsString(gameData);
            mStompClient.send("/app/sendToPlayerv2", gameDataJson).subscribe();
        } catch (JsonProcessingException e) {
            Log.e(TAG, "JSON processing error", e);
        }

        return gameData;
    }

    private GameData getGameDataWithCowsBullsCount(JSONObject jsonObject) throws JSONException {
        String playerWord = jsonObject.getString("word").toUpperCase();
        char[] hostArray = hostWord.toCharArray();
        char[] playerArray = playerWord.toCharArray();

        int cows = 0, bulls = 0;
        for (int i = 0; i < hostArray.length; i++) {
            if (hostArray[i] == playerArray[i]) bulls++;
            if (hostWord.contains(String.valueOf(playerArray[i]))) cows++;
        }
        cows -= bulls;

        GameData gameData = new GameData();
        gameData.setFromUser(hostUsername);
        gameData.setRoomName(roomName);
        gameData.setPlayerName(jsonObject.getString("playerName"));
        gameData.setWord(playerWord);
        gameData.setBullsCount(bulls);
        gameData.setCowsCount(cows);

        return gameData;
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }
}
