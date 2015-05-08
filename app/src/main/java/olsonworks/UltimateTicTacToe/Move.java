package olsonworks.UltimateTicTacToe;

/**
 * Created by Peter Olson on 5/7/2015.
 */
public class Move {

    //coordinate values of game move
    public int gameX;
    public int gameY;
    public int tileX;
    public int tileY;

    //creates new empty move
    public Move(){

    }

    //creates a new move from XY coordinates'
    public Move(int tX, int tY, int gX,int gY){
        gameX = gX;
        gameY = gY;
        tileX = tX;
        tileY = tY;
    }

    //creates a new move from combined Integer value'
    public Move(int move){
        //parse integer value into the Game and Tile numbers
        int game = (int)(move/10);
        int tile = (move % 10);
        //parse integers into X and Y coordinates
        gameX = game % 3;
        gameY = (int)(game/3);
        tileX = tile % 3;
        tileY = (int)(tile/3);
    }

    //returns the moves in an integer array with the tile and game coordinates in XY format (tileX,tileY,gameX.gameY format).
    public int[] getMoveArray(){
        return new int[]{tileX,tileY,gameX,gameY};
    }

    //returns the move as a 2 digit integer (for use with old system of integers. will be depreciated I think
    public int getMoveInteger(){
        return ((((gameY*3)+gameX)*10) + ((tileY*3)+tileX));
    }

}
