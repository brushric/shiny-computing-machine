package edu.msu.brushric.theflockinggame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;


public class WinActivity extends ActionBarActivity {

    /**
     * the game manager for the current game
     */
    private GameManager manager;
    private int score;

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
            player.setText(getString(R.string.wins) + " " + manager.getWinnerName());
        }


    }

    public void onReset(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
