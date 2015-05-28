package nomorecookies.UltimateTicTacToe;

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
    private boolean mIsTied;
    private int mWinner;

    //What to do when a new SubBoard is created with no inputs (blank)
    public SubBoard(){
        //turn each of the members of the array to state 0, and set there to be no winner
        for (int i = 0; i < mTiles.length; i++) {
            for (int j = 0; i < mTiles[0].length; i++) {
                mTiles[i][j] = 0;
            }
        }
        mIsWon = false;
        mIsTied = false;
        mWinner = 0;
    }

    //create a new SubBoard by copying a different SubBoard.
    // Called just as normally making a new instance, but just with an argument of the SubBoard to be copied.
    // I can see this coming in handy if the AI wants to make dummy games to think through it's moves.
    public SubBoard(SubBoard reference){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mTiles[i][j] = reference.getTile(i,j);
            }
        }
        mIsWon = reference.isWon();
        mIsTied = reference.isTied();
        mWinner = reference.getWinner();
    }

    //The only thing it should really have to do (returns false if that is an illegal move):
    public boolean makeMove(Move move){
        //Make sure it is a legal move, and about with false return if illegal
        if(!isLegalMove(move)) return false;

        //Make the intended move
        mTiles[move.getTileX()][move.getTileY()] = (move.isPlayer1Turn() ? 1 : 2);

        //check for tie
        mIsTied = checkTie();
        if (mIsTied) {
            Log.d("GAME MOVE:","subGame just tied");
        }

        //check for win.
        mIsWon = checkWin();
        if (mIsWon) {
            mWinner = (move.isPlayer1Turn() ? 1 : 2);
            Log.d("GAME MOVE:","player " + (move.isPlayer1Turn() ? 1 : 2) + " just won tile " + move.getGameX() + "," + move.getGameY());
        }
        return true;
    }

    //sets the location of the move to be 0 (available). returns true if this this "un-wins" the game
    public boolean undoMove (Move move){
        boolean wasWinningMove = isWon();

        mTiles[move.getTileX()][move.getTileY()] = 0;
        mIsWon = checkWin();

        return wasWinningMove;
    }

    //Double checks to make sure move is legal
    public boolean isLegalMove(Move move){
        return !(mIsWon || mTiles[move.getTileX()][move.getTileY()] != 0 || mIsTied);
    }

    //Double checks to make sure move is legal
    public boolean isLegalMove(int tileX, int tileY){
        return !(mIsWon || mTiles[tileX][tileY] != 0 || mIsTied);
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
        if(mTiles[2][0] !=0 && mTiles[2][0] == mTiles[1][1] && mTiles[2][0] == mTiles[0][2]) {return true;}

        return false;
    }

    public boolean checkTie(){
        int count = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (mTiles[i][j] == 0) {return false;}
            }
        }

        return true;
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

    public int getTileHash(){
        int hashCode = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                hashCode = (hashCode*10) + mTiles[x][y];
            }
        }
        return hashCode;
    }

    public int[] getTileHashWithRotations(){
        int hashCode[] = new int[4];

        //get normal hashCode
        hashCode[0] = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                hashCode[0] = (hashCode[0]*10) + mTiles[x][y];
            }
        }
        //get hash code rotated 90o clockwise
        hashCode[1] = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 2; y >= 0; y--) {
                hashCode[1] = (hashCode[1]*10) + mTiles[x][y];
            }
        }

        //get hash code rotated 180o clockwise
        hashCode[2] = 0;
        for (int y = 2; y >= 0; y--) {
            for (int x = 2; x >= 0; x--) {
                hashCode[2] = (hashCode[2]*10) + mTiles[x][y];
            }
        }

        //get hash code rotated 270o clockwise
        hashCode[3] = 0;
        for (int x = 2; x >= 0; x--) {
            for (int y = 0; y > 3; y++) {
                hashCode[3] = (hashCode[3]*10) + mTiles[x][y];
            }
        }

        return hashCode;
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

    public boolean isTied(){ return mIsTied;}

    public void setTied(boolean isTied) {
        this.mIsWon = isTied;
    }

    public boolean isFinishedGame(){
        return (mIsTied || mIsWon);
    }

    public int getWinner() {
        return mWinner;
    }

    public void setWinner(int winner) {
        this.mWinner = winner;
    }

}