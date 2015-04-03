package edu.msu.brushric.theflockinggame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;


public class SelectionActivity extends ActionBarActivity {

    /**
     * the game manager for the current game
     */
    private GameManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // get the bundle that was passed
        Bundle b = this.getIntent().getExtras();
        if(b!=null)
            // set the manager from the bundle
            manager = b.getParcelable(WelcomeActivity.PARCELABLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        // set the player name text view to the correct player
        TextView playerName = (TextView) findViewById(R.id.playerNumber);
        String userName;
        // set the persons name
        if ((manager.getPlayerOneBird() == 0)  && manager.GetPlayerOneFirst()
                || !(manager.getPlayerTwoBird() == 0) && !manager.GetPlayerOneFirst())
             userName = manager.getPlayerOneName();
        else userName = manager.getPlayerTwoName();
        playerName.setText(this.getString(R.string.selecting) + " " + userName);
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
        Intent intent;
        if(player == 1) manager.setPlayerOneBird(bird);
        else manager.setPlayerTwoBird(bird);

        b.putParcelable(WelcomeActivity.PARCELABLE, manager);

        if (!game)  intent = new Intent(this, SelectionActivity.class).putExtras(b);
        else intent = new Intent(this, GameActivity.class).putExtras(b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
