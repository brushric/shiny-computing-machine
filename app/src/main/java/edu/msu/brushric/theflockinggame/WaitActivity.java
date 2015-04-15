package edu.msu.brushric.theflockinggame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class WaitActivity extends ActionBarActivity {

    public static String PARCELABLE = "Parceable";
    private GameManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);


        Bundle b = this.getIntent().getExtras();
        if(b!=null)
            // set the manager from the bundle
            manager = b.getParcelable(WelcomeActivity.PARCELABLE);

        String text = "";
        switch(manager.getCurrWaitType()) {
            case 0:
                text = "Waiting for a second player...";
                break;
            case 1:
                text = "Waiting for other player to select/place their bird.";
                break;
        }
        TextView waitText = (TextView) findViewById(R.id.waitText);
        waitText.setText(text);
        executeOnDelay();
    }

    public void executeOnDelay() {
        // TODO: poll server (happens every 10 sec)
        delay();
    }

    public void delay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                executeOnDelay();
            }
        }, 10000);
    }

    public void goToNextActivity(View view) {
        switch(manager.getCurrWaitType()) {
            case 0:
                // the player is waiting on another player to join
                // once they do, you go to selection
                goToSelection();
                break;
            case 1:
                // you have placed your bird and are waiting for the other person to place theirs
                goToSelection();
                break;
        }
    }

    public void goToSelection() {
        // we go straight to selection, cause both can choose at the same time
        Bundle b = new Bundle();
        Intent intent = new Intent(this, SelectionActivity.class);
        b.putParcelable(PARCELABLE, manager);
        intent.putExtras(b);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_wait, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logOut() {

        SharedPreferences settings =
                getSharedPreferences(manager.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(manager.REMEMBER, false);
        editor.commit();

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
