package edu.msu.brushric.theflockinggame;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ryan on 2/15/15.
 * This class manages the state of the game. it implements parcable to
 * allow us to pass the object in a bundle from activity to activity
 */
public class GameManager implements Parcelable {

    public static int NEWGAME = 0;
    public static int PLACEMENT = 1;

    public final static String PREFERENCES = "preferences";
    public final static String REMEMBER = "remember";
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";

    private int currWaitType = 0;

    public int getCurrWaitType() {
        return currWaitType;
    }

    public void setCurrWaitType(int type) {
        currWaitType = type;
    }

    public GameManager() {
    }

    /**
     * array list of birds placed
     */
    ArrayList<BirdPiece> arrayList = new ArrayList<>();

    /**
     * player ones bird
     */
    private int playerOneBird;
    /**
     * player twos bird
     */
    private int playerTwoBird;

    /**
     * Number of birds placed
     * Starts at 0
     */
    private int score = 0;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * current round, starts at one
     */
    private int round = 1;

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    private boolean imFirstPlayer = true;

    public void setImFirstPlayer(boolean val) {
        this.imFirstPlayer = val;
    }

    public boolean getImFirstPlayer() {
        return imFirstPlayer;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    private String winner = "";

    public ArrayList<BirdPiece> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<BirdPiece> arrayList) {
        this.arrayList = arrayList;
    }

    public void addPiece(BirdPiece p) {
        arrayList.add(p);
    }

    public int getPlayerOneBird() {
        return playerOneBird;
    }

    public void setPlayerOneBird(int playerOneBird) {
        this.playerOneBird = playerOneBird;
    }

    public int getPlayerTwoBird() {
        return playerTwoBird;
    }

    public void setPlayerTwoBird(int playerTwoBird) {
        this.playerTwoBird = playerTwoBird;
    }

    public boolean GetPlayerOneFirst() {
        return round % 2 == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imFirstPlayer ? 1 : 0);
        dest.writeString(winner);
        dest.writeInt(playerOneBird);
        dest.writeInt(playerTwoBird);
        dest.writeTypedList(arrayList);
        dest.writeInt(round);
        dest.writeInt(score);
        dest.writeString(username);
    }

    /**
     * a parceable creator for the parceble
     */
    public static final Parcelable.Creator<GameManager> CREATOR
            = new Parcelable.Creator<GameManager>() {
        public GameManager createFromParcel(Parcel in) {
            return new GameManager(in);
        }

        public GameManager[] newArray(int size) {
            return new GameManager[size];
        }
    };

    /**
     * Private constructor for the parcablizer which will parcabalize all members
     *
     * @param in a parcel to put everything into
     */
    private GameManager(Parcel in) {
        imFirstPlayer = in.readInt() == 1;
        winner = in.readString();
        playerOneBird = in.readInt();
        playerTwoBird = in.readInt();
        arrayList = in.createTypedArrayList(BirdPiece.CREATOR);
        round = in.readInt();
        score = in.readInt();
        username = in.readString();
    }

    public void saveXml(XmlSerializer xml) throws IOException {

        //xml.startTag(null, "birds");
        for (BirdPiece item : arrayList) {
            xml.startTag(null, "bird");
            xml.attribute(null, "type", Integer.toString(item.getType()));
            xml.attribute(null, "x", Float.toString(item.getX()));
            xml.attribute(null, "y", Float.toString(item.getY()));
            xml.endTag(null, "bird");
        }
        //xml.endTag(null, "birds");
        xml.startTag(null, "score");
        xml.attribute(null, "score", Integer.toString(score));
        xml.endTag(null, "score");
    }

    public void loadXml(XmlPullParser xml, Context context) throws IOException, XmlPullParserException {

        // Create a new set of birds
        final ArrayList<BirdPiece> newBirds = new ArrayList<BirdPiece>();
        int type = 0;

        while (xml.nextTag() == XmlPullParser.START_TAG) {
            //while (xml.nextTag() != XmlPullParser.END_DOCUMENT) {
            if (xml.getName().equals("bird")) {
                type = Integer.parseInt(xml.getAttributeValue(null, "type"));

                BirdPiece piece = new BirdPiece(context, type);

                piece.setX(Float.parseFloat(xml.getAttributeValue(null, "x")));
                piece.setY(Float.parseFloat(xml.getAttributeValue(null, "y")));
                newBirds.add(piece);
            } else if (xml.getName().equals("score")) {
                setScore(Integer.parseInt(xml.getAttributeValue(null, "score")));
            }
            xml.nextTag();
        }

        arrayList = newBirds;
    }

    public void logout(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Cloud cloud = new Cloud();
                final boolean over = cloud.logoutUser(username);

            }
        }).start();
    }
}
