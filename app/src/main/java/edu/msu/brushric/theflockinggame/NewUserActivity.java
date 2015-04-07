package edu.msu.brushric.theflockinggame;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NewUserActivity extends ActionBarActivity {


    private UserCreateTask mAuthTask = null;
    private EditText mUserView;
    private EditText mPassword1View;
    private EditText mPassword2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mUserView = (EditText) findViewById(R.id.usernameText);
        mPassword1View = (EditText) findViewById(R.id.passwordText1);
        mPassword2View = (EditText) findViewById(R.id.passwordText2);
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
        else if(field == 2) message = "User could not be created.";
        else message = "passwords don't match.";

        // create and show the toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * When the user presses the create button, do basic field testing, then create
     */
    public void onCreateUser(View view) {
        if (mAuthTask != null) {
            return;
        }

        // Store values at the time of the login attempt.
        String username = mUserView.getText().toString();
        String password1 = mPassword1View.getText().toString();
        String password2 = mPassword2View.getText().toString();

        boolean cancel = false;

        // Check for a valid username/pass
        if (TextUtils.isEmpty(username)) {
            ShowToast(0);
            cancel = true;
        }
        if (TextUtils.isEmpty(password1)) {
            ShowToast(1);
            cancel = true;
        }
        if (TextUtils.isEmpty(password2)) {
            ShowToast(1);
            cancel = true;
        }
        if(password2 != password1) {
            ShowToast(3);
            cancel = true;
        }

        // only continue to auth if not canceled
        if(!cancel) {
            mAuthTask = new UserCreateTask(username, password1);
            mAuthTask.execute((Void) null);
        }
    }


    /**
     * Attempts user log in through a task
     */
    public class UserCreateTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;

        UserCreateTask(String user, String password) {
            mUser = user;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt to create user using mUser and mPassword, if failure, return false
            Cloud cloud = new Cloud();
            boolean ok = cloud.addNewUser(mUser, mPassword);
            if(!ok){
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                goBack();
            } else {
                ShowToast(2);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void goBack() {
        finish();
    }
}
