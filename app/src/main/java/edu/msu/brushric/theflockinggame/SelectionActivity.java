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


public class SelectionActivity extends ActionBarActivity {

    /**
     * the game manager for the current game
     */
    private GameManager manager;

    private boolean load = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // get the bundle that was passed
        Bundle b = this.getIntent().getExtras();
        if(b!=null) {
            // set the manager from the bundle
            manager = b.getParcelable(WelcomeActivity.PARCELABLE);
            load = b.getBoolean("LOAD");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

    }

    /**
     * OnClick function if an ostritch is selected
     * @param view that is selected
     */
    public void onSelectOstritch(View view) {
        ChangeActivity(BirdPiece.OSTRICH);
    }
    /**
     * OnClick function if an parrot is selected
     * @param view that is selected
     */
    public void onSelectParrot(View view) {
        ChangeActivity(BirdPiece.PARROT);
    }
    /**
     * OnClick function if an seagull is selected
     * @param view that is selected
     */
    public void onSelectSeagull(View view) {
        ChangeActivity(BirdPiece.SEAGULL);
    }
    /**
     * OnClick function if an robin is selected
     * @param view that is selected
     */
    public void onSelectRobin(View view) {
        ChangeActivity(BirdPiece.ROBIN);
    }
    /**
     * OnClick function if an bananaquit is selected
     * @param view that is selected
     */
    public void onSelectBananaquit(View view) {
        ChangeActivity(BirdPiece.BANANAQUIT);
    }

    /**
     * function that switches the activity based on what user selection it is
     */
    void ChangeActivity(int bird){
        // determine to go to next round or let the other player pick
        if (manager.getPlayerOneBird() ==  0 && manager.GetPlayerOneFirst())
            NextTurn(1, bird, false);
        else if ((manager.getPlayerTwoBird() == 0) && !manager.GetPlayerOneFirst())
            NextTurn(2, bird, false);
        else if (manager.getPlayerOneBird() ==  0 && !manager.GetPlayerOneFirst())
            NextTurn(1,bird, true);
        else
            NextTurn(2, bird, true);
    }

    /**
     * Function moves to the next turn or to the game activity
     * @param player number for the person who just went
     * @param bird to be added to that player
     * @param game flag for if you go to the game or not. if true moves to the game activity
     *             if it is false it goes to selection for the other player
     */
    private void NextTurn(int player, int bird, boolean game){
        Bundle b = new Bundle();
//        if(player == 1) manager.setPlayerOneBird(bird);
//        else manager.setPlayerTwoBird(bird);
        manager.setPlayerOneBird(bird);
        manager.setPlayerTwoBird(bird);

        manager.setCurrWaitType(GameManager.PLACEMENT);

        b.putParcelable(WelcomeActivity.PARCELABLE, manager);
        b.putBoolean("LOAD", load);
        Intent intent = new Intent(this, GameActivity.class).putExtras(b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                getSharedPreferences(GameManager.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(GameManager.REMEMBER, false);
        editor.apply();

        manager.logout();

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
