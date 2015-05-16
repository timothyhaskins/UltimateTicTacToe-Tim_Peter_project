package olsonworks.UltimateTicTacToe;

import android.widget.ImageView;

/**
 * Created by FeverPDX on 5/15/2015.
 */
public class gameList {

    private int mGameX;
    private int mGameY;
    private ImageView mImageViewResource;

    public gameList(int gameX, int gameY, ImageView imageViewResource) {
        this.mGameX = gameX;
        this.mGameY = gameY;
        this.mImageViewResource = imageViewResource;
    }

    public int getGameX() {
        return mGameX;
    }

    public void setGameX(int gameX) {
        mGameX = gameX;
    }

    public int getGameY() {
        return mGameY;
    }

    public void setGameY(int gameY) {
        mGameY = gameY;
    }

    public ImageView getImageViewResource() {
        return mImageViewResource;
    }

    public void setImageViewResource(ImageView imageViewResource) {
        mImageViewResource = imageViewResource;
    }

}


