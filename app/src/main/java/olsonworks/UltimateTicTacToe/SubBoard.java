package olsonworks.UltimateTicTacToe;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Olson on 4/29/2015.
 *
 * This class is the container for all information of any subgame of tic tac toe in the larger game.
 */
public class SubBoard {

    //declaration of variables
    private int[][] mTiles = new int[3][3];
    private boolean mIsWon;
    private int winner;

    //What to do when a new SubBoard is created with no inputs (blank)
    public SubBoard(){
        //turn each of the members of the array to state 0, and set there to be no winner
        for (int i = 0; i < mTiles.length; i++) {
            for (int j = 0; i < mTiles[0].length; i++) {
                mTiles[i][j] = 0;
            }
        }
        mIsWon = false;
    }

    //create a new SubBoard by copying a different SubBoard.
    // Called just as normally making a new instance, but just with an argument of the SubBoard to be copied.
    // I can see this coming in handy if the AI wants to make dummy games to think through it's moves.
    public SubBoard(SubBoard reference){
        mTiles = reference.getTiles();
        mIsWon = reference.isWon();
        winner = reference.getWinner();
    }

    //The only thing it should really have to do (returns false if that is an illegal move):
    public boolean makeMove(Move move){
        //Make sure it is a legal move, and about with false return if illegal
        if(!isLegalMove(move)) return false;

        //Make the intended move
        mTiles[move.getTileX()][move.getTileY()] = (move.isPlayer1Turn() ? 1 : 2);

        //check for win.
        mIsWon = checkWin();
        if (mIsWon) {
            winner = (move.isPlayer1Turn() ? 1 : 2);
            Log.d("GAME MOVE:","player " + (move.isPlayer1Turn() ? 1 : 2) + " just won tile " + move.getGameX() + "," + move.getGameY());
        }
        return true;
    }

    //sets the location of the move to be 0 (available). returns true if this this "un-wins" the game
    public boolean  undoMove (Move move){
        boolean wasWinningMove = isWon();

        mTiles[move.getTileX()][move.getTileY()] = 0;
        mIsWon = checkWin();

        return wasWinningMove;
    }

    //Double checks to make sure move is legal
    public boolean isLegalMove(Move move){
        if(mIsWon || mTiles[move.getTileX()][move.getTileY()] != 0) return false;
        return true;
    }

    //Double checks to make sure move is legal
    public boolean isLegalMove(int tileX, int tileY){
        if(mIsWon || mTiles[tileX][tileY] != 0) return false;
        return true;
    }

    //check for win. This might not be easy to make elegant. There really must be a mathy way to do this
    public boolean checkWin(){
        //check rows
        for (int y = 0; y < 3; y++) {
            if(mTiles[0][y] !=0 && mTiles[0][y] == mTiles[1][y] && mTiles[0][y] == mTiles[2][y]) {return true;}
        }
        //check columns
        for (int x = 0; x < 3; x++) {
            if(mTiles[x][0] !=0 && mTiles[x][0] == mTiles[x][1] && mTiles[x][0] == mTiles[x][2]) {return true;}
        }
        //check diagonals
        if(mTiles[0][0] !=0 && mTiles[0][0] == mTiles[1][1] && mTiles[0][0] == mTiles[2][2]) {return true;}
        if(mTiles[2][0] !=0 && mTiles[2][0] == mTiles[1][1] && mTiles[2][0] == mTiles[2][0]) {return true;}

        return false;
    }

    //will return a list of all available moves, but I have to remember how lists are implemented first...
    public List listAvailableMoves(){
        List moves = new ArrayList();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (isLegalMove(x,y)) {
                    moves.add(new int[]{x,y});
                }
            }
        }
        return moves;
    }

    //getters and setters for local variables
    public int[][] getTiles() {
        return mTiles;
    }

    public int getTile(int tileX, int tileY) {
        return mTiles[tileX][tileY];
    }

    public void setTiles(int[][] mTiles) {
        for (int i = 0; i < mTiles.length; i++) {
            for (int j = 0; j < mTiles[0].length; j++) {
                this.mTiles[i][j] = mTiles[i][j];
            }
        }
    }

    public boolean isWon() {
        return mIsWon;
    }

    public void setWon(boolean won) {
        this.mIsWon = won;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }


}