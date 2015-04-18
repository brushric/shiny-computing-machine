package edu.msu.brushric.theflockinggame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;


public class GameActivity extends ActionBarActivity{ //implements AlertDialog.OnDismissListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = (GameView) this.findViewById(R.id.gameView);
        game = gameView.getGame();
        game.setGameActivity(this);

        // get the bundle that was passed
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            // set the manager from the bundle
            manager = b.getParcelable(WelcomeActivity.PARCELABLE);
            boolean load = b.getBoolean("LOAD");

            if(load){
                loadBoard();
            }
        }

        if (manager.getImFirstPlayer()) {
            game.initializeBirds(manager, new BirdPiece(this, manager.getPlayerOneBird()));
        } else {
            game.initializeBirds(manager, new BirdPiece(this, manager.getPlayerTwoBird()));
        }

        // set the player name text view to the correct player
        TextView playerNameView = (TextView) findViewById(R.id.playerName);
        //String playerName;
        // set the persons name
        //if (!(manager.getPlayerOneBird() == 0)  && manager.GetPlayerOneFirst()
        //        || manager.getPlayerTwoBird() == 0 && !manager.GetPlayerOneFirst())
        //    playerName = manager.getPlayerOneName();
        //else playerName = manager.getPlayerTwoName();
        playerNameView.setText(this.getString(R.string.placing));
    }

    public void onPlace(View view) {
        Bundle b = new Bundle();

        // Check to see if the bird being placed is out of bounds
        if (game.outOfBounds() || game.collision()) {

            manager.setWinner("Other Player");
//            if(manager.getImFirstPlayer())
//                manager.setWinner("other player");
//            else
//                manager.setWinner("you");

            //TODO: let the other player know i lost
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Cloud cloud = new Cloud();
                    final boolean ok = cloud.saveBoard(manager);
                }
            }).start();

            Intent intent = new Intent(this, WinActivity.class);
            b.putParcelable(WelcomeActivity.PARCELABLE, manager);
            intent.putExtras(b);
            startActivity(intent);
            return;
        }

        // add the piece that is being placed to the game manager array
        manager.addPiece(game.getDragging());

        // Increment score because bird was placed
        manager.setScore(manager.getScore() + 1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cloud cloud = new Cloud();
                final boolean ok = cloud.saveBoard(manager);

                NextRound();
            }
        }).start();


    }

    private void NextRound(){
        Bundle b = new Bundle();
        Intent intent;
        // Player 1 has to wait for player 2 to choose and place
        manager.setRound(manager.getRound() + 1);
        if(manager.getImFirstPlayer()) {
            intent = new Intent(this, WaitActivity.class);
            manager.setCurrWaitType(GameManager.PLACEMENT);
        }
        else { // player 2 should let player 1 know he placed and then they both go to the selection (let player 1 know im now player 1)
            intent = new Intent(this, SelectionActivity.class);
            manager.setImFirstPlayer(true);
        }

        b.putParcelable(WelcomeActivity.PARCELABLE, manager);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                getSharedPreferences(GameManager.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(GameManager.REMEMBER, false);
        editor.apply();

        manager.logout();

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void loadBoard(){
        final Context context1 = this;
        final LoadingDlg dlg = new LoadingDlg();
        dlg.show(getFragmentManager(), "load");
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML
                Cloud cloud = new Cloud();
                InputStream stream = cloud.loadBoard();

//                if (cancel) {
//                    return;
//                }

                // Test for an error
                boolean fail = stream == null;
                if (!fail) {
                    try {
                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "toucan");
                        String status = xml.getAttributeValue(null, "status");
                        if (status.equals("yes")) {
                            manager.loadXml(xml, context1);
                        } else {
                            fail = true;
                        }

                    } catch (IOException ex) {
                        fail = true;
                    } catch (XmlPullParserException ex) {
                        fail = true;
                    } finally {
                        try {
                            stream.close();
                        } catch (IOException ex) {
                        }
                    }
                }
                if (manager.getImFirstPlayer()) {
                    game.initializeBirds(manager, new BirdPiece(context1, manager.getPlayerOneBird()));
                } else {
                    game.initializeBirds(manager, new BirdPiece(context1, manager.getPlayerTwoBird()));
                }

                dlg.dismiss();
            }
    }).start();
    }

//    public void loadBoard(){
//        final Context context1 = this;
//        //new Thread(new Runnable() {
//
//            //@Override
//            //public void run() {
//                // Create a cloud object and get the XML
//                Cloud cloud = new Cloud();
//                InputStream stream = cloud.loadBoard();
//
////                if (cancel) {
////                    return;
////                }
//
//                // Test for an error
//                boolean fail = stream == null;
//                if (!fail) {
//                    try {
//                        XmlPullParser xml = Xml.newPullParser();
//                        xml.setInput(stream, "UTF-8");
//
//                        xml.nextTag();      // Advance to first tag
//                        xml.require(XmlPullParser.START_TAG, null, "toucan");
//                        String status = xml.getAttributeValue(null, "status");
//                        if (status.equals("yes")) {
//                            manager.loadXml(xml, context1);
//                        } else {
//                            fail = true;
//                        }
//
//                    } catch (IOException ex) {
//                        fail = true;
//                    } catch (XmlPullParserException ex) {
//                        fail = true;
//                    } finally {
//                        try {
//                            stream.close();
//                        } catch (IOException ex) {
//                        }
//                    }
//                }
//                if (manager.getImFirstPlayer()) {
//                    game.initializeBirds(manager, new BirdPiece(context1, manager.getPlayerOneBird()));
//                }
//                else {
//                    game.initializeBirds(manager, new BirdPiece(context1, manager.getPlayerTwoBird()));
//                }
//            //}
//        //}).start();
//    }
}