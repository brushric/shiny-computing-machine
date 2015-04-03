package edu.msu.brushric.theflockinggame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;


public class GameActivity extends ActionBarActivity {

    /**
     * Game view that draws the pieces
     */
    private GameView gameView;

    /**
     * Game manager to manage the game state
     */
    private GameManager manager;

    /**
     * Game manager to manage the game state
     */
    private Game game;

    /**
     * flag for whos turn it is
     */
    private boolean playerOnePlaced = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = (GameView) this.findViewById(R.id.gameView);
        game = gameView.getGame();
        game.setGameActivity(this);

        // get the bundle that was passed
        Bundle b = this.getIntent().getExtras();
        if (b != null)
            // set the manager from the bundle
            manager = b.getParcelable(WelcomeActivity.PARCELABLE);

        if (!(manager.getPlayerOneBird() == 0)  && manager.GetPlayerOneFirst()
                || manager.getPlayerTwoBird() == 0 && !manager.GetPlayerOneFirst()) {
            game.initializeBirds(manager, new BirdPiece(this, manager.getPlayerOneBird()));
        }
        else {
            game.initializeBirds(manager, new BirdPiece(this, manager.getPlayerTwoBird()));
            playerOnePlaced = true;
        }

        // set the player name text view to the correct player
        TextView playerNameView = (TextView) findViewById(R.id.playerName);
        String playerName;
        // set the persons name
        if (!(manager.getPlayerOneBird() == 0)  && manager.GetPlayerOneFirst()
                || manager.getPlayerTwoBird() == 0 && !manager.GetPlayerOneFirst())
            playerName = manager.getPlayerOneName();
        else playerName = manager.getPlayerTwoName();
        playerNameView.setText(this.getString(R.string.placing) + " " + playerName);
    }

    public void onPlace(View view) {
        Bundle b = new Bundle();

        // Check to see if the bird being placed is out of bounds
        if (game.outOfBounds() || game.collision()) {
            String playerName;
            if (!(manager.getPlayerOneBird() == 0)  && manager.GetPlayerOneFirst()
                    || manager.getPlayerTwoBird() == 0 && !manager.GetPlayerOneFirst())
                playerName = manager.getPlayerTwoName();
            else playerName = manager.getPlayerOneName();

            manager.setPlayerWinnerName(playerName);

            Intent intent = new Intent(this, WinActivity.class);
            b.putParcelable(WelcomeActivity.PARCELABLE, manager);
            intent.putExtras(b);
            startActivity(intent);
            return;
        }

        // add the piece that is beeing placed to the game manager array
        manager.addPiece(game.getDragging());

        // Increment score because bird was placed
        manager.setScore(manager.getScore() + 1);

        // determine to go to next round or end game
        if (!playerOnePlaced && manager.GetPlayerOneFirst())
            NextTurn(1);
        else if (!(manager.getPlayerTwoBird() == 0) && !manager.GetPlayerOneFirst())
            NextTurn(2);
        else if (!playerOnePlaced && !manager.GetPlayerOneFirst())
            NextRound(1);
        else
            NextRound(2);
    }

    private void NextTurn(int player){
        if (player == 1) manager.setPlayerOneBird(0);
        else manager.setPlayerTwoBird(0);

        Bundle b = new Bundle();

        // add the parcable to the bundle and return to the game activity
        Intent intent = new Intent(this, GameActivity.class);
        b.putParcelable(WelcomeActivity.PARCELABLE, manager);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(b);
        startActivity(intent);
    }
    private void NextRound(int player){
        if (player == 1) manager.setPlayerOneBird(0);
        else manager.setPlayerTwoBird(0);

        Bundle b = new Bundle();
        Intent intent = new Intent(this, SelectionActivity.class);
        manager.setPlayerTwoBird(0);

        // add the parcable to the bundle and go to the selection activity
        manager.setRound(manager.getRound() + 1);
        b.putParcelable(WelcomeActivity.PARCELABLE, manager);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(b);
        startActivity(intent);
    }
}