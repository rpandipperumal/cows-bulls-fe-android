package com.techtweakz.cowsandbulls;

import static com.techtweakz.cowsandbulls.HostActivity.TAG;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.StompClient;

public class HostViewActivity extends AppCompatActivity {


    private StompClient mStompClient;

    private CompositeDisposable compositeDisposable;

    String hostUsername;
    String roomName;

    String hostWord;

    String playerName;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    private HashMap<String, Integer> playerTabMap = new HashMap<>();

    private HashMap<String, Integer> playerUserNameMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view);

        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        Intent intent = getIntent();
        hostUsername = intent.getStringExtra(HostActivity.PERSON_ID);
        roomName = intent.getStringExtra(HostActivity.ROOM_NAME);
        playerName=intent.getStringExtra(HostActivity.PERSON_NAME);

        hostWord= intent.getStringExtra(HostActivity.HOST_WORD);

        mStompClient = StompUtil.createStompClient(hostUsername);


        String hostTopic = "/topic/" + roomName + "/host";

        HashMap<String,Integer> userNameMap = new HashMap<>();
        AtomicInteger numOfPlayers = new AtomicInteger();

        // Receive usermessage
        Disposable dispTopic = mStompClient.topic(hostTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    JSONObject jsonObject = new JSONObject(topicMessage.getPayload());
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    GameData gameData = sendMessageToPlayer(jsonObject);
                    // Check if the player's username is in the mapping
                    String playerUsername = gameData.getFromUser();
                    int tabIndex;
                    if (playerTabMap.containsKey(playerUsername)) {
                        tabIndex = playerTabMap.get(playerUsername);
                    } else {
                        // If not, assign a new tab index
                        tabIndex = playerTabMap.size();
                        playerTabMap.put(playerUsername, tabIndex);
                        numOfPlayers.getAndIncrement(); // Increment the total number of players
                    }
                    String displayWord = gameData.getWord() + " Cows : " + gameData.getCowsCount() + " Bulls : " + gameData.getBullsCount();
                   // MyPagerAdapter pagerAdapter = new MyPagerAdapter(this, numOfPlayers.get(), displayWord);
                    MyPagerAdapter pagerAdapter = new MyPagerAdapter(this, numOfPlayers.get(), tabIndex, displayWord);
                    viewPager2.setAdapter(pagerAdapter);

                    new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                        // Set the tab titles here based on the player's username
                        for (Map.Entry<String, Integer> entry : playerTabMap.entrySet()) {
                            if (entry.getValue() == position) {
                                tab.setText("Player " + entry.getKey());
                                break;
                            }
                        }
                    }).attach();

                   /* new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                        // Set the tab titles here (e.g., "Player 1", "Player 2", etc.)
                        tab.setText("Player " + (position + 1));
                    }).attach();*/
                    new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                        // Set the tab titles here based on the player's username
                        for (Map.Entry<String, Integer> entry : playerTabMap.entrySet()) {
                            if (entry.getValue() == position) {
                                tab.setText("Player " + entry.getKey());
                                break;
                            }
                        }
                    }).attach();
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });
  /*      if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }*/
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(dispTopic);
    }


    public GameData sendMessageToPlayer(JSONObject jsonObject) throws JSONException {

        ObjectMapper mapper = new ObjectMapper();
        GameData gameData = getGameDataWithCowsBullsCount(jsonObject);

        String gameDataJson = "";
        try {
            gameDataJson = mapper.writeValueAsString(gameData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        mStompClient.send("/app/sendToPlayerv2", gameDataJson).subscribe();

        return gameData;
    }

    private GameData getGameDataWithCowsBullsCount(JSONObject jsonObject) throws JSONException {
        //String hostWord ="CROW";
        int wordLength = hostWord.length();
        String playerWord = jsonObject.getString("word").toUpperCase();
        char[] hostArray = hostWord.toCharArray();
        char[] playerArray = playerWord.toCharArray();
        Set<Character> hostSet = new HashSet<>();
        for (int i = 0; i < hostArray.length; i++) {
            hostSet.add(hostArray[i]);
        }
        int cows = 0;
        int bulls = 0;
        for (int i = 0; i < wordLength; i++) {
            if (hostArray[i] == playerArray[i]) bulls++;
        }
        for (int i = 0; i < wordLength; i++) {
            if (hostSet.contains(playerArray[i])) cows++;
        }
        if (0 < bulls) cows = cows - bulls;
        GameData gameData = new GameData();
        gameData.setFromUser(hostUsername);
        gameData.setRoomName(roomName);
        gameData.setPlayerName(playerName);
        gameData.setToUser(jsonObject.getString("fromUser"));
        gameData.setWord(playerWord);
        gameData.setBullsCount(bulls);
        gameData.setCowsCount(cows);
        return gameData;
    }
}

