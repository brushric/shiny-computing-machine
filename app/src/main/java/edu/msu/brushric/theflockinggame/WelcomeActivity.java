package edu.msu.brushric.theflockinggame;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class WelcomeActivity extends ActionBarActivity {

    /**
     * Tag for adding a player number to intents for tracking
     * which player is selecting their bird
     */
    public static String PARCELABLE = "Parceable";

    /**
     * GameManager used to track the game state
     */
    private GameManager manager = new GameManager();

    private EditText mUserView;
    private EditText mPasswordView;
    private CheckBox rememberCheck;
    private volatile boolean cancel = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_welcome);

        mUserView = (EditText) findViewById(R.id.usernameText);
        mPasswordView = (EditText) findViewById(R.id.passwordText);
        rememberCheck = (CheckBox) findViewById(R.id.rememberBox);

        SharedPreferences settings =
                getSharedPreferences(GameManager.PREFERENCES, MODE_PRIVATE);
        boolean remember = settings.getBoolean(GameManager.REMEMBER, false);

        if(remember) {
            String userString = settings.getString(GameManager.USERNAME, "");
            String passString = settings.getString(GameManager.PASSWORD, "");
            mUserView.setText(userString);
            mPasswordView.setText(passString);
            rememberCheck.setChecked(true);
            //logIn(userString, passString, findViewById(R.id.frameWelcome));
        }
    }

    /**
     * On click the remember checkbox
     * @param view that was clicked
     */
    public void onRemember(View view) {
        if(rememberCheck.isChecked()) {
            SharedPreferences settings =
                    getSharedPreferences(GameManager.PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(GameManager.REMEMBER, true);
            editor.putString(GameManager.USERNAME, mUserView.getText().toString());
            editor.putString(GameManager.PASSWORD, mPasswordView.getText().toString());
            editor.commit();
        } else {

            SharedPreferences settings =
                    getSharedPreferences(GameManager.PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(GameManager.REMEMBER, false);
            editor.commit();
        }
    }

    /**
     * on click function for the help button
     * @param view that was clicked
     */
    public void onHelp(View view){
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        // Parameterize the builder
        builder.setTitle(R.string.how_to);
        builder.setMessage(R.string.help_text);
        builder.setPositiveButton(android.R.string.ok, null);


        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Shows a toast to tell the user to enter a username/pass
     * @param field which field
     */
    private void ShowToast(int field){
        // set the message for the toast
        String message;
        if (field ==  0) message = "Please enter a username.";
        else if(field == 1) message = "Please enter a password.";
        else message = "Incorrect username or password.";

        // create and show the toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * User presses new user, send them to new activity
     */
    public void onNewUser(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }

    /**
     * User presses log in, does basic check on username/pass before
     * beginning authentication task
     */
    public void onLogIn(View view) {
        // Store values at the time of the login attempt.
        String username = mUserView.getText().toString();
        manager.setUsername(username);
        String password = mPasswordView.getText().toString();
        logIn(username, password, view);
    }

    void logIn(String u, String p, View view) {
        final String username = u;
        final String password = p;
        cancel = false;

        // Check for a valid username/pass
        if (TextUtils.isEmpty(username)) {
            ShowToast(0);
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            ShowToast(1);
            cancel = true;
        }

        final View finalView = view;
        // only continue to auth if not canceled
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(cancel)
                    return;

                Cloud cloud = new Cloud();
                final boolean ok = cloud.loginUser(username, password);

                finalView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!ok){
                            ShowToast(2);
                        } else {
                            goToWait();
                        }
                    }
                });

            }
        }).start();
    }

    public void goToWait() {
        Bundle b = new Bundle();

        // if there's a player on the server ready to play,
        // hook up with them then go to selection
        // set manager.setImFirstPlayer to false

        // if we're the first one, go to wait
        Intent intent = new Intent(this, WaitActivity.class);
        manager.setImFirstPlayer(true);
        manager.setCurrWaitType(GameManager.NEWGAME);
        b.putParcelable(PARCELABLE, manager);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}
