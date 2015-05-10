package olsonworks.UltimateTicTacToe;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Olson on 5/3/2015.
 */
public class GameController {

    private Board mGameBoard;
    private Boolean mIsGameOver;
    private int mGameType; //0=PvP, 1=PvE, 2=EvE
    private Move mUndoMove;
    private boolean mIsPlayer1Turn;
    private AIPlayer mAIPlayer1;
    private AIPlayer mAIPlayer2;
    private List<Move> mMoveHistory;

    public void GameController() {
        //set up game
        mGameBoard = new Board();
        mIsGameOver = false;
        mMoveHistory = new ArrayList<Move>();

        //Setup game characteristics
        mGameType = 0;
        mIsPlayer1Turn = true;

        //make any AIs that are needed
        if (mGameType > 0){
            mAIPlayer1 = new AIPlayer(true,0);
            if (mGameType == 2){
                mAIPlayer2 = new AIPlayer(false,0);
            }
        }
    }


    public void GameController(int type) {
        //set up game
        mGameBoard = new Board();
        mIsGameOver = false;

        //Setup game characteristics
        mGameType = type;
        mIsPlayer1Turn = true;

        //make any AIs that are needed
        if (mGameType > 0){
            mAIPlayer1 = new AIPlayer(true,0);
            if (mGameType == 2){
                mAIPlayer2 = new AIPlayer(false,0);
            }
        }
    }

    public void takeTurn(Move move){
        //same as above, but with given inputs
        if (!mIsGameOver) {
            if (mGameBoard.isLegalMove(move)) {
                if (!mIsGameOver) {
                    mGameBoard.makeMove(move);
                    mMoveHistory.add(move);
                    mIsGameOver = mGameBoard.checkWin();
                    //Changes current Player
                    mIsPlayer1Turn = !mIsPlayer1Turn;
                } else {
                    Log.d("GAME MOVE:", "invalid move");
                }
            } else {
                Log.d("GAME MOVE:", "game over");
            }
            logBoard(mGameBoard);
        }
    }

    //get random board move
    public void makeRandomMove(){
        List<Move> moves = mGameBoard.listAvailableMoves();
        Move move = moves.get((int) (Math.random() * moves.size()));

        takeTurn(move);
    }


    public Move getLastMove() {
        mMoveHistory.remove(0);
        mUndoMove = mMoveHistory.get(0);
        return mUndoMove;
    }

    public void logBoard(Board board){
        String output = "";
        int[][][][] boardSpace = board.indexBoard();
        Log.d("GAME MOVE:", "new move");
        Log.d("GAME MOVE:", "-------------");
        for (int iGameY = 0; iGameY < 3; iGameY++) {
            for (int iTileY = 0; iTileY < 3; iTileY++) {
                output = "|";
                for (int iGameX = 0; iGameX < 3; iGameX++) {
                    for (int iTileX = 0; iTileX < 3; iTileX++) {
                        switch (boardSpace[iGameX][iGameY][iTileX][iTileY]){
                            case 1:
                                output += "X";
                                break;
                            case 2:
                                output += "O";
                                break;
                            default:
                                output += "*";
                                break;
                        }
                    }
                    output +="|";
                }
                Log.d("GAME MOVE:",output);
            }
            Log.d("GAME MOVE:", "-------------");
        }
    }

    public Board getGameBoard() {
        return mGameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.mGameBoard = gameBoard;
    }

    public Boolean getIsGameOver() {
        return mIsGameOver;
    }

    public void setIsGameOver(Boolean isGameOver) {
        this.mIsGameOver = isGameOver;
    }

    public int getGameType() {
        return mGameType;
    }

    public void setGameType(int gameType) {
        this.mGameType = gameType;
    }
}
