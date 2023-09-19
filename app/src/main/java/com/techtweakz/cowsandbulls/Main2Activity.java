    package com.techtweakz.cowsandbulls;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.EditText;

    import com.google.android.gms.auth.api.signin.GoogleSignIn;
    import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

    import androidx.appcompat.app.AppCompatActivity;

    public class Main2Activity extends AppCompatActivity {

        public static final String EXTRA_MESSAGE = "com.techtweakz.MESSAGE";

        public static final String ROOM_NAME = "com.techtweakz.MESSAGE.ROOM.NAME";

        public static final String EXTRA_MESSAGE_PLAYER = "com.techtweakz.MESSAGE.PLAYER";

        String personName;

        String personGivenName;
        String personId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            if (acct != null) {
                personName = acct.getDisplayName();
               personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                 personId = acct.getId();
              //  Uri personPhoto = acct.getPhotoUrl();
            }

        }

        /** Called when the user taps the Send button */
        public void openPlayerActivity(View view) {
            Intent intent = new Intent(Main2Activity.this, PlayerActivity.class);
            EditText editText = (EditText) findViewById(R.id.editText2);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE_PLAYER, personId);
            intent.putExtra(ROOM_NAME,message);
            startActivity(intent);
        }
        /** Called when the user taps the Send button */
        public void openHostActivity(View view) {
            Intent intent = new Intent(Main2Activity.this, HostActivity.class);
            EditText editText = (EditText) findViewById(R.id.editText2);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, personId);
            intent.putExtra(ROOM_NAME,message);
            startActivity(intent);

        }

    }
