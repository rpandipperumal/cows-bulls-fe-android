package com.techtweakz.cowsandbulls;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.StompClient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class HostActivity extends AppCompatActivity {


    private StompClient mStompClient;
    private Disposable mRestPingDisposable;
    private CompositeDisposable compositeDisposable;
    public static final String TAG = "techtweakz";
    String hostUsername;
    String roomName;
    String hostWord;

    private Button button;
    private EditText editText;
    private ListView listView;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        Intent intent = getIntent();
        hostUsername = intent.getStringExtra(Main2Activity.EXTRA_MESSAGE);
        roomName = intent.getStringExtra(Main2Activity.ROOM_NAME);

        mStompClient = StompUtil.createStompClient(hostUsername);
        //    String hostTopic = "/topic/room/host";

        String hostTopic = "/topic/" + roomName + "/host";

        editText = findViewById(R.id.editText1);
        button = findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editText1);
                hostWord = editText.getText().toString().toUpperCase();
                editText.setText("");
                button.setEnabled(false);
            }
        });

        listView = findViewById(R.id.listView1);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // Receive usermessage
        Disposable dispTopic = mStompClient.topic(hostTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    JSONObject jsonObject = new JSONObject(topicMessage.getPayload());
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    //Toast.makeText(this, jsonObject.getString("word"), Toast.LENGTH_SHORT).show();
                    // setText(jsonObject.getString("word"));
                    sendMessageToPlayer(jsonObject);
                    //  addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(dispTopic);


    }


    public void sendMessageToPlayer(JSONObject jsonObject) throws JSONException {

        ObjectMapper mapper = new ObjectMapper();
        GameData gameData = getGameDataWithCowsBullsCount(jsonObject);

        String gameDataJson = "";
        try {
            gameDataJson = mapper.writeValueAsString(gameData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String displayWord = gameData.getWord() + "Cows : " + gameData.getCowsCount() + "Bulls : " + gameData.getBullsCount();

        adapter.add(displayWord);

        mStompClient.send("/app/sendToPlayerv2", gameDataJson).subscribe();
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
        gameData.setToUser(jsonObject.getString("fromUser"));
        gameData.setWord(playerWord);
        gameData.setBullsCount(bulls);
        gameData.setCowsCount(cows);
        return gameData;
    }
}
