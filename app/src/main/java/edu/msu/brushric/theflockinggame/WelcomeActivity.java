package edu.msu.brushric.theflockinggame;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
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

    /**
     * text views that contain player one and two names
     */
    TextView playerOne, playerTwo;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_welcome);

        // set the two player name text boxes
        playerOne = (TextView) findViewById(R.id.playerOneName);
        playerTwo = (TextView) findViewById(R.id.playerTwoName);
    }

    /**
     * OnClick method for the start button this will check if the user names
     * are valid, if they are not it will show a tost telling the user to
     * enter valid names. if they are valid they will add them to the game manager and
     * start the selection activity
     * @param view that was clicked
     */
    public void onStart(View view) {
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

            else ShowTost(2);
        }
        else ShowTost(1);
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
     * Shows a tost to tell the user to enter a players name
     * @param player who needs to enter a name
     */
    private void ShowTost(int player){
        // set the message for the toast
        String message;
        if (player ==  1) message = "Please enter name for player 1.";
        else message = "Please enter name for player 2.";

        // create and show the toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
