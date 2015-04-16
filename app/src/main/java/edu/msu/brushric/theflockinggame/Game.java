package edu.msu.brushric.theflockinggame;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;

public class Game {

    /**
     * Percentage of the display width or height that
     * is occupied by the puzzle.
     */
    final static float SCALE_IN_VIEW = 0.9f;

    /**
     * Paint for outlining the area the puzzle is in
     */
    private Paint outlinePaint;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Background for the game
     */
    private Bitmap gameBackground;


    /**
     * Collection of puzzle pieces
     */
    public ArrayList<BirdPiece> birds = new ArrayList<BirdPiece>();

    /**
     * Top margin in pixels
     */
    private int marginY;

    /**
     * The size of the puzzle in pixels
     */
    private int gameSize;

    /**
     * How much we scale the puzzle pieces
     */
    private float scaleFactor;

    public BirdPiece getDragging() {
        return dragging;
    }

    /**
     * This variable is set to a piece we are dragging. If
     * we are not dragging, the variable is null.
     */
    private BirdPiece dragging = null;

    /**
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    /**
     * This variable is set to a piece we are dragging. If
     * we are not dragging, the variable is null.
     */
    //private Bird dragging = null;

    private GameView gameView;

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    private GameActivity gameActivity;

    public Game(Context context) {
        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setColor(0xff000000);

        // Load the solved puzzle image
        gameBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.sparty_done);
    }

    public void initializeBirds(GameManager manager, BirdPiece draggingPiece) {
        // loop through all the existing pieces and set the bitmap for them in a temp array
        // so they can be drawn
        for ( BirdPiece item : manager.getArrayList()){
            BirdPiece piece = new BirdPiece(gameActivity, item.getType());
            piece.setX(item.getX());
            piece.setY(item.getY());
            birds.add(piece);
        }


        dragging = draggingPiece;
    }

    public void draw(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        gameSize = (int)(minDim * SCALE_IN_VIEW);

        // Compute the margins so we center the game
        marginX = (wid - gameSize) / 2;
        marginY = (hit - gameSize) / 2;

        //Draw the border of the game area
        canvas.drawLine(marginX, marginY, marginX + gameSize, marginY, outlinePaint);
        canvas.drawLine(marginX, marginY, marginX, marginY + gameSize, outlinePaint);
        canvas.drawLine(marginX + gameSize, marginY + gameSize, marginX  + gameSize, marginY, outlinePaint);
        canvas.drawLine(marginX + gameSize, marginY + gameSize, marginX, marginY + gameSize, outlinePaint);

        scaleFactor = (float)gameSize / (float)gameBackground.getWidth();

        for(BirdPiece bird : birds) {
            bird.draw(canvas, marginX, marginY, gameSize, scaleFactor);
            bird.setPuzzleSize(gameSize);
        }
        dragging.draw(canvas, marginX, marginY, gameSize, scaleFactor);
        dragging.setPuzzleSize(gameSize);
    }

    /**
     * Handle a touch message. This is when we get an initial touch
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {
        lastRelX = x;
        lastRelY = y;

        return true;
    }

    /**
     * Handle a release of a touch message.
     * @param x x location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {
        return true;
    }

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        //
        // Convert an x,y location to a relative location in the
        // puzzle.
        //
        float relX = (event.getX() - marginX) / (float)gameBackground.getWidth();
        float relY = (event.getY() - marginY) / (float)gameBackground.getWidth();

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return onReleased(view, relX, relY);

            case MotionEvent.ACTION_MOVE:
                dragging.move(relX - lastRelX, relY - lastRelY);
                lastRelX = relX;
                lastRelY = relY;
                view.invalidate();
                return true;
        }

        return false;
    }

    public boolean outOfBounds() {
        float rightX = (dragging.getX() + dragging.getWidth());
        float rightY = (dragging.getY() + dragging.getHeight());
        return dragging.getX() < 0 || dragging.getY() < 0 ||
                (dragging.getX() + dragging.getWidth()) > 1 ||
                (dragging.getY() + dragging.getHeight()) > 1;
    }

    public boolean collision() {
        for(BirdPiece bird : birds) {
            if(dragging.collisionTest(bird)) {
                return true;
            }
        }
        return false;
    }
}

