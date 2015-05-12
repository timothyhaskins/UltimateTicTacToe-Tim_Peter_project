package olsonworks.UltimateTicTacToe;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Olson on 4/29/2015.
 */
public class Board {

    private SubBoard[][] mGames;
    //Which subGame you must play in next, -1 if any
    private int mNextGameX;
    private int mNextGameY;
    private int[] mWonGames = new int[3];
    private ArrayList mWonGamesList = new ArrayList();

    //Create new blank Board
    public Board(){
        mGames = new SubBoard[3][3];
        mNextGameX = -1;
        mNextGameY = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mGames[i][j] = new SubBoard();
            }
        }
    }

    //Create new board as copy of incoming Board
    public Board(Board template){
        setGames(template.getGames());
        mNextGameX = template.getNextGameX();
        mNextGameY = template.getNextGameY();
    }

    //This makes the actual moves, and returns the next SubBoard that must be played in as a 2d array(x,y). Returns -1.-1 for "Freemove"
    public int[] makeMove(Move move){
        //Makes move, logs if it tries an illegal move
        if (!mGames[move.getGameX()][move.getGameY()].makeMove(move)){
            //Checks if this makes a "Freemove", or sets where the next move will be
            if(mGames[move.getTileX()][move.getTileY()].isWon()){
                mNextGameX = -1;
                mNextGameY = -1;
            }else{
                mNextGameX = move.getTileX();
                mNextGameY = move.getTileY();
            }
        }else{
            Log.d("GAME MOVE:","Illegal move");
        }

        return new int[]{mNextGameX, mNextGameY};
    }

    public void undoMove(Move move){

    }

    //Make sure it is a legal move
    public boolean isLegalMove(Move move){
        return mGames[move.getGameX()][move.getGameY()].isLegalMove(move);
    }

    //Returns a list of arrays that contain the X and Y coordinates for each game that has yet to be won
    public List<int[]> getAvailableGames(){
        //Makes the array so that it will only hold int[]s
        List<int[]> availableGames = new ArrayList<int[]>();

        //Iterate through the 9 subgames to check if it's been won
        for (int i = 0; i < mGames.length; i++) {
            for (int j = 0; j < mGames[i].length; j++) {
                //If the game has not been won, then add an int to the array with it's X and Y coordinates
                if (!mGames[i][j].isWon()){
                    //this is a nice way to make a new array and fill it with the values in the same line( with the curly brackets)
                    availableGames.add(new int[]{i, j});
                }
            }
        }
        //send back the filled list
        return availableGames;
    }


    //will return a list of all available moves, but I have to remember how lists are implemented first...
    public List listAvailableMoves(){
        List moves = new ArrayList();

        //iterates through possible moves and adds them to list if legal move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //checks to see if this game is the allowed game, or you have an any move
                if ((mNextGameX == i && mNextGameY ==j) || mNextGameX == -1) {
                    for (int x = 0; x < 9; x++) {
                        for (int y = 0; y < 9; y++) {
                            //it the move legal?
                            Move newMove = new Move(i, j, x, y, true);
                            if (mGames[i][j].isLegalMove(newMove)) {
                                //add to list of available moves, with the first digit as game, second digit as tile
                                moves.add(newMove);
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    //Returns a 3x3x3x3 array of integers that represents the board state
    public int[][][][] indexBoard(){
        int[][][][] stateSpace = new int[3][3][3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        stateSpace[i][j][k][l] = mGames[i][j].getTile(k,l);
                    }
                }
            }
        }

        return stateSpace;
    }

    public boolean checkWin(){
        //check rows
        for (int y = 0; y < 3; y++) {
            if(mGames[0][y].isWon() && mGames[0][y].getWinner() == mGames[1][y].getWinner() && mGames[0][y].getWinner() == mGames[2][y].getWinner()) {return true;}
        }
        //check columns
        for (int x = 0; x < 3; x++) {
            if(mGames[x][0].isWon() && mGames[x][0].getWinner() == mGames[x][1].getWinner() && mGames[x][0].getWinner() == mGames[x][2].getWinner()) {return true;}
        }
        //check diagonals
        if(mGames[0][0].isWon() && mGames[0][0].getWinner() == mGames[1][1].getWinner() && mGames[0][0].getWinner() == mGames[2][2].getWinner()) {return true;}
        if(mGames[2][0].isWon() && mGames[2][0].getWinner() == mGames[1][1].getWinner() && mGames[2][0].getWinner() == mGames[2][0].getWinner()) {return true;}

        return false;
    }
     /* public addWonGame() {
        mWonGames[0] = move.getTileX();
        mWonGames[1] = move.getTileY();
        mWonGames[2] = winner;
        mWonGamesList.add(0, mWonGames);
    }
    */

    //Getters and setters
    public SubBoard[][] getGames() {
        return mGames;
    }

    public void setGames(SubBoard[][] games) {
        for (int i = 0; i < games.length; i++) {
            for (int j = 0; j < games.length; j++) {
                this.mGames[i][j] = new SubBoard(games[i][j]);
            }
        }
    }

    public int getNextGameX() {
        return mNextGameX;
    }

    public int getNextGameY() {
        return mNextGameY;
    }

    public void setNextGame(int nextGameX, int nextGameY) {
        this.mNextGameX = nextGameX;
        this.mNextGameX = nextGameX;
    }


}

