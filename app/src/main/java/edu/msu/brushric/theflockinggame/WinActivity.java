package edu.msu.brushric.theflockinggame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class WinActivity extends ActionBarActivity {

    /**
     * the game manager for the current game
     */
    private GameManager manager;
    private int score;
    private volatile boolean cancel = false;

    TextView scoreBox, player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        scoreBox = (TextView) findViewById(R.id.points);
        player = (TextView) findViewById(R.id.winner);

        // get the bundle that was passed
        Bundle b = this.getIntent().getExtras();
        if(b!=null) {
            // set the manager from the bundle
            manager = b.getParcelable(WelcomeActivity.PARCELABLE);
            score =  manager.getScore();
            scoreBox.setText(getString(R.string.score) + " " + score);
            player.setText(getString(R.string.wins) + " " + manager.getWinner());
        }


    }

    public void onReset(View view) {
        SharedPreferences settings =
                getSharedPreferences(manager.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(manager.REMEMBER, true);
        editor.commit();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(cancel)
                    return;

                Cloud cloud = new Cloud();
                final boolean over = cloud.gameOver();

            }
        }).start();

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
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

        manager.logout();

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
