package com.techtweakz.cowsandbulls;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.StompClient;

public class PlayerActivity extends AppCompatActivity {


    private StompClient mStompClient;
    private Disposable mRestPingDisposable;
    private CompositeDisposable compositeDisposable;

    private EditText editText;
    private Button button;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    public static final String TAG = "techtweakz";

    String playerUsername;

    String playerDisplayName;
    String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button1);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString();
                //adapter.add(value);
                sendMessageToHost(v);
                editText.setText("");
            }
        });


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        playerUsername = intent.getStringExtra(Main2Activity.PERSON_ID);
        playerDisplayName =intent.getStringExtra(Main2Activity.PERSON_NAME);

        roomName = intent.getStringExtra(Main2Activity.ROOM_NAME);

        mStompClient = StompUtil.createStompClient(playerUsername);

        //String queue = "/queue/player/" + playerUsername;

        //String playerTopic = "/topic/room/player";

        String playerTopic = "/topic/" + roomName + playerUsername + "/player";

        // player subcribe to the user queue
        Disposable dispQueue = mStompClient.topic(playerTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(queueMessage -> {
                    JSONObject jsonObject = new JSONObject(queueMessage.getPayload());
                    if (jsonObject.getString("toUser").equalsIgnoreCase(playerUsername)) {Log.i("xxxxx", jsonObject.getString("bullsCount"));
                        adapter.add( jsonObject.getString("word") + " Cows : " + jsonObject.getString("cowsCount") + " Bulls :  "
                                + jsonObject.getString("bullsCount"));
                    }
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(dispQueue);
    }
//

    /**
     * Called when the user taps the Send button
     */
    public void sendMessageToHost(View view) {
        EditText editText = findViewById(R.id.editText);
        ObjectMapper mapper = new ObjectMapper();
        GameData gameData = new GameData();
        gameData.setFromUser(playerUsername);
        gameData.setRoomName(roomName);
        gameData.setPlayerName(playerDisplayName);
        gameData.setWord(editText.getText().toString().toUpperCase());
        String gameDataJson = "";
        try {
            gameDataJson = mapper.writeValueAsString(gameData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        mStompClient.send("/app/sendToHost", gameDataJson).subscribe();
    }
}
