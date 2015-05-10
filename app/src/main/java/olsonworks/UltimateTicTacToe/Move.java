package olsonworks.UltimateTicTacToe;

/**
 * Created by Peter Olson on 5/7/2015.
 */
public class Move {

    //coordinate values of game move
    public int mTileX;
    public int mTileY;
    public int mGameX;
    public int mGameY;
    public boolean mIsPlayer1Turn;

    //creates new empty move
    public Move(){}

    //creates a new move from XY coordinates'
    public Move(int tileX, int tileY, int gameX, int gameY, boolean player1Turn){
        mTileX = tileX;
        mTileY = tileY;
        mGameX = gameX;
        mGameY = gameY;
        mIsPlayer1Turn = player1Turn;
    }

    public int getTileX() {
        return mTileX;
    }

    public void setTileX(int tileX) {
        mTileX = tileX;
    }

    public int getTileY() {
        return mTileY;
    }

    public void setTileY(int tileY) {
        mTileY = tileY;
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

    public boolean getPlayer1Turn() {
        return mIsPlayer1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        mIsPlayer1Turn = player1Turn;
    }

}
