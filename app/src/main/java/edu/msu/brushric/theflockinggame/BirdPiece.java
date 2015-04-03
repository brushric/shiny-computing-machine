package edu.msu.brushric.theflockinggame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gamer on 2/11/2015.
 */
public class BirdPiece implements Parcelable{

    /**
     * Constant for an ostrich bird piece
     */
    public static final int OSTRICH = 1;

    /**
     * Constant for a robin bird piece
     */
    public static final int ROBIN = 2;

    /**
     * Constant for a parrot bird piece
     */
    public static final int PARROT = 3;

    /**
     * Constant for a seagull bird piece
     */
    public static final int SEAGULL = 4;

    /**
     * Constant for a bananaquit bird piece
     */
    public static final int BANANAQUIT = 5;


    /**
     * The image for the actual bird.
     */
    private Bitmap bird;

    /**
     * Rectangle that is where our bird is.
     */
    private Rect rect;

    /**
     * Rectangle we will use for intersection testing
     */
    private Rect overlap = new Rect();

    /**
     * x location between 0 and 1
     */
    private float x = 0;

    /**
     * y location between 0 and 1
     */
    private float y = 0;

    // x location in pixels
    private int xInPixels = 0;

    // y location in pixels
    private int yInPixels = 0;

    // Scale of the image being drawn
    private float scale = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * Type of bird that this is
     */
    private int type;

    /**
     * resource id for the piece
     */
    private int resID;

    public void setPuzzleSize(int puzzleSize) {
        this.puzzleSize = puzzleSize;
    }

    // Size of the game area in pixels
    private int puzzleSize;

    public BirdPiece(Context context, int id) {
        type = id;
        switch (id){
            case BANANAQUIT:
                resID = R.drawable.bananaquit;
                break;
            case OSTRICH:
                resID = R.drawable.ostrich;
                break;
            case ROBIN:
                resID = R.drawable.robin;
                break;
            case PARROT:
                resID = R.drawable.parrot;
                break;
            case SEAGULL:
                resID = R.drawable.seagull;
                break;
        }
        bird = BitmapFactory.decodeResource(context.getResources(), resID);
        rect = new Rect();
        setRect();
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
        setRect();
    }

    private void setRect() {
        rect.set(xInPixels, yInPixels, xInPixels + bird.getWidth(), yInPixels + bird.getHeight());
    }

    public boolean hit(float testX, float testY) {
        int pX = (int)((testX - x));
        int pY = (int)((testY - y));

        if(pX < 0 || pX >= bird.getWidth() ||
                pY < 0 || pY >= bird.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (bird.getPixel(pX, pY) & 0xff000000) != 0;
    }

    /**
     * Collision detection between two birds. This object is
     * compared to the one referenced by other
     * @param other Bird to compare to.
     * @return True if there is any overlap between the two birds.
     */
    public boolean collisionTest(BirdPiece other) {
        // Do the rectangles overlap?
        if(!Rect.intersects(rect, other.rect)) {
            return false;
        }

        // Determine where the two images overlap
        overlap.set(rect);
        overlap.intersect(other.rect);

        // We have overlap. Now see if we have any pixels in common
        for(int r=overlap.top; r<overlap.bottom;  r++) {
            int aY = (int)((r - yInPixels));
            int bY = (int)((r - other.yInPixels));

            for(int c=overlap.left;  c<overlap.right;  c++) {

                int aX = (int)((c - xInPixels));
                int bX = (int)((c - other.xInPixels));

                if( (bird.getPixel(aX, aY) & 0x80000000) != 0 &&
                        (other.bird.getPixel(bX, bY) & 0x80000000) != 0) {
                    //Log.i("collision", "Overlap " + r + "," + c);
                    return true;
                }
            }
        }

        return false;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return ((float)bird.getWidth() / (float)puzzleSize) * scale;
    }

    public float getHeight() {
        return ((float)bird.getHeight() / (float)puzzleSize) * scale;
    }

    /**
     * Draw the puzzle piece
     * @param canvas Canvas we are drawing on
     * @param marginX Margin x value in pixels
     * @param marginY Margin y value in pixels
     * @param puzzleSize Size we draw the puzzle in pixels
     * @param scaleFactor Amount we scale the puzzle pieces when we draw them
     */
    public void draw(Canvas canvas, int marginX, int marginY, int puzzleSize, float scaleFactor) {
        xInPixels = marginX + (int)(x * puzzleSize);
        yInPixels = marginY + (int)(y * puzzleSize);

        scale = scaleFactor;

        setRect();

        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * puzzleSize, marginY + y * puzzleSize);

        // Scale it to the right size
        canvas.scale(scaleFactor, scaleFactor);

        // Draw the bitmap
        canvas.drawBitmap(bird, 0, 0, null);


        canvas.restore();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(x);
        dest.writeFloat(y);
        dest.writeInt(type);
    }

    public static final Parcelable.Creator<BirdPiece> CREATOR
            = new Parcelable.Creator<BirdPiece>() {
        public BirdPiece createFromParcel(Parcel in) {
            return new BirdPiece(in);
        }

        public BirdPiece[] newArray(int size) {
            return new BirdPiece[size];
        }
    };

    private BirdPiece(Parcel in){
        x = in.readFloat();
        y = in.readFloat();
        type = in.readInt();
    }

}
