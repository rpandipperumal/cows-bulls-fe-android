    package com.techtweakz.cowsandbulls;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.viewpager.widget.ViewPager;

    import io.reactivex.android.schedulers.AndroidSchedulers;
    import io.reactivex.disposables.CompositeDisposable;
    import io.reactivex.disposables.Disposable;
    import io.reactivex.schedulers.Schedulers;
    import ua.naiksoftware.stomp.StompClient;

    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Parcelable;
    import android.util.Log;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;

    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.google.android.gms.games.Game;
    import com.google.android.material.tabs.TabLayout;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    public class HostActivity extends AppCompatActivity {

        public static final String HOST_WORD = "com.techtweakz.HOST.WORD";


        public static final String PERSON_ID = "com.techtweakz.PERSON.ID";

        public static final String ROOM_NAME = "com.techtweakz.MESSAGE.ROOM.NAME";

        public static final String TAG = "techtweakz";

        String hostWord;

        private Button button;
        private EditText editText;

        String hostUsername;
        String roomName;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_host);

            Intent intent = getIntent();
            hostUsername = intent.getStringExtra(Main2Activity.PERSON_ID);
            roomName = intent.getStringExtra(Main2Activity.ROOM_NAME);

            editText = findViewById(R.id.editText1);
            button = findViewById(R.id.button3);



            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = findViewById(R.id.editText1);
                    hostWord = editText.getText().toString().toUpperCase();
                    editText.setText("");
                    button.setEnabled(false);
                    Intent intent = new Intent(HostActivity.this, HostViewActivity.class);
                    intent.putExtra(HOST_WORD, hostWord);
                    intent.putExtra(PERSON_ID, hostUsername);
                    intent.putExtra(ROOM_NAME,roomName);
                    startActivity(intent);
                }
            });

        }
    }
