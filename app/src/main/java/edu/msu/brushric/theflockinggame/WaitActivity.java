package edu.msu.brushric.theflockinggame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;


public class WaitActivity extends ActionBarActivity {

    public static String PARCELABLE = "Parceable";
    private GameManager manager;
    private volatile boolean cancel = false;
    Handler handler = new Handler();

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cloud cloud = new Cloud();
                final String status = cloud.serverPoll(manager.getUsername());

//                if(cancel)
//                    return;

                if(status.equals("no")) {
                    delay();
                }else if(status.equals("yes")) {
                    goToSelection();
                }else if(status.equals("winner")) {
                    winGame();
                }if(status.equals("update")) {
                    loadBoard();
                }
            }
        }).start();
    }

    public void delay() {
        //final Handler handler = new Handler();
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

    public void onCancel(){
        //cancel = true;
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

        manager.logout();

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void winGame(){
        manager.setWinner("you");
        Bundle b = new Bundle();
        Intent intent = new Intent(this, WinActivity.class);
        b.putParcelable(WelcomeActivity.PARCELABLE, manager);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void loadBoard(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML
                Cloud cloud = new Cloud();
                InputStream stream = cloud.loadBoard();

                if (cancel) {
                    return;
                }

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

                            while (xml.nextTag() == XmlPullParser.START_TAG) {
                                if (xml.getName().equals("bird")) {
                                    if (cancel) {
                                        return;
                                    }

                                    // load the game
                                    manager.loadXml(xml);
                                    break;
                                }

                                Cloud.skipToEndTag(xml);
                            }
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
            goToSelection();
            }
        }).start();
    }
}