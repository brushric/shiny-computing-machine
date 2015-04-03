package edu.msu.brushric.theflockinggame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ryan on 2/15/15.
 * This class manages the state of the game. it implements parcable to
 * allow us to pass the object in a bundle from activity to activity
 */
public class GameManager implements Parcelable{

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

    /** Number of birds placed
     *  Starts at 0
     */
    private int score = 0;

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

    /**
     * player ones name
     */
    private String playerOneName;

    /**
     * player twos name
     */
    private String playerTwoName;

    public void setPlayerWinnerName(String playerWinnerName) {
        this.playerWinnerName = playerWinnerName;
    }

    private String playerWinnerName;

    public ArrayList<BirdPiece> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<BirdPiece> arrayList) {
        this.arrayList = arrayList;
    }

    public void addPiece(BirdPiece p){
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

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getWinnerName() {
        return playerWinnerName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public boolean GetPlayerOneFirst(){ return round % 2 == 1;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playerOneName);
        dest.writeString(playerTwoName);
        dest.writeString(playerWinnerName);
        dest.writeInt(playerOneBird);
        dest.writeInt(playerTwoBird);
        dest.writeTypedList(arrayList);
        dest.writeInt(round);
        dest.writeInt(score);
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
     * @param in a parcel to put everything into
     */
    private GameManager(Parcel in){
        playerOneName = in.readString();
        playerTwoName = in.readString();
        playerWinnerName = in.readString();
        playerOneBird = in.readInt();
        playerTwoBird = in.readInt();
        arrayList = in.createTypedArrayList(BirdPiece.CREATOR);
        round = in.readInt();
        score = in.readInt();
    }

}
