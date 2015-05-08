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
    public boolean mPlayer1Turn;

    //creates new empty move
    public Move(){}

    //creates a new move from XY coordinates'
    private Move(int tileX, int tileY, int gameX, int gameY, boolean player1Turn){
        mTileX = tileX;
        mTileY = tileY;
        mGameX = gameX;
        mGameY = gameY;
        mPlayer1Turn = !player1Turn;
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
        return mPlayer1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        mPlayer1Turn = player1Turn;
    }


    //creates a new move from combined Integer value'
    public Move(int move){
        //parse integer value into the Game and Tile numbers
        int game = (int)(move/10);
        int tile = (move % 10);
        //parse integers into X and Y coordinates
        mGameX = game % 3;
        mGameY = (int)(game/3);
        mTileX = tile % 3;
        mTileY = (int)(tile/3);
    }

    //returns the moves in an integer array with the tile and game coordinates in XY format (mTileX,mTileY,mGameX.mGameY format).
    public int[] getMoveArray(){
        return new int[]{mTileX, mTileY, mGameX, mGameY};
    }

    //returns the move as a 2 digit integer (for use with old system of integers. will be depreciated I think
    public int getMoveInteger(){
        return ((((mGameY *3)+ mGameX)*10) + ((mTileY *3)+ mTileX));
    }

}
