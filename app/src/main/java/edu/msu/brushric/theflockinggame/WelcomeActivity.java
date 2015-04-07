package edu.msu.brushric.theflockinggame;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
    private volatile boolean cancel = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_welcome);

        mUserView = (EditText) findViewById(R.id.usernameText);
        mPasswordView = (EditText) findViewById(R.id.passwordText);
    }

    /**
     * OnClick method for the start button this will check if the user names
     * are valid, if they are not it will show a tost telling the user to
     * enter valid names. if they are valid they will add them to the game manager and
     * start the selection activity
     * @param view that was clicked
     */
    /*public void onStart(View view) {
        Intent intent = new Intent(this, SelectionActivity.class);
        Bundle b = new Bundle();

        if(!playerOne.getText().toString().equals("")) {
            manager.setPlayerOneName(playerOne.getText().toString());

            if (!playerTwo.getText().toString().equals("")){
                manager.setPlayerTwoName(playerTwo.getText().toString());
                b.putParcelable(PARCELABLE, manager);
                intent.putExtras(b);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            else ShowToast(2);
        }
        else ShowToast(1);
    }*/

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
        final String username = mUserView.getText().toString();
        final String password = mPasswordView.getText().toString();

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

        // only continue to auth if not canceled
        new Thread(new Runnable() {

            @Override
            public void run() {
                if(cancel)
                    return;

                Cloud cloud = new Cloud();
                boolean ok = cloud.loginUser(username, password);
                if(!ok){
                    ShowToast(2);
                } else {
                    goToWait();
                }
            }
        }).start();

    }

    public void goToWait() {
        Intent intent = new Intent(this, WaitActivity.class);
        startActivity(intent);
    }
}
