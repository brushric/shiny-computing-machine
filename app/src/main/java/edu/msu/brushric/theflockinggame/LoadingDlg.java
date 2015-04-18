package edu.msu.brushric.theflockinggame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;


public class LoadingDlg extends DialogFragment {

    private final static String ID = "id";

    /**
     * Set true if we want to cancel
     */
    private volatile boolean cancel = false;


    /**
     * Create the dialog box
     */

    public Dialog onCreateDialog(final GameActivity gameActivity) {
        cancel = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.loading);

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                cancel = true;
            }
        });


        // Create the dialog box
        final AlertDialog dlg = builder.create();

        // Get a reference to the view we are going to load into
        final GameView view = (GameView)getActivity().findViewById(R.id.gameView);
        //gameActivity.loadBoard();

        new Thread(new Runnable() {
            @Override
            public void run() {
                gameActivity.loadBoard();
            }
        }).start();



        return dlg;
    }
}