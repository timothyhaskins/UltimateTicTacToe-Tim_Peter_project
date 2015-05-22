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
    private boolean mIsPlayer1Turn;
    private AIPlayer mAIPlayer1;
    private AIPlayer mAIPlayer2;
    private List<Move> mMoveHistory;

    public GameController() {
        //set up game
        mGameBoard = new Board();
        mIsGameOver = false;
        mMoveHistory = new ArrayList<Move>();

        //Setup game characteristics
        mGameType = 0;
        mIsPlayer1Turn = true;
    }


    public GameController(int type) {
        //set up game
        mGameBoard = new Board();
        mIsGameOver = false;
        mMoveHistory = new ArrayList<Move>();

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

    public int[] takeTurn(Move move){
        int[] nextMove = new int[2];
        //Verifies the game isn't over, and the move is legal
        if (!mIsGameOver) {
            if (mGameBoard.isLegalMove(move)) {
                //make the move
                nextMove = mGameBoard.makeMove(move);

                //adds the move to the list of moves
                mMoveHistory.add(move);

                //checks to see if this won the game
                mIsGameOver = mGameBoard.checkWin();

                //switches the active player
                mIsPlayer1Turn = !mIsPlayer1Turn;
            }else {
                Log.d("GAME MOVE:", "invalid move");
            }
            //outputs game board to the console
            logBoard(mGameBoard);
        }else {
            nextMove[0] = mGameBoard.getNextGameX();
            nextMove[1] = mGameBoard.getNextGameY();
            Log.d("GAME MOVE:", "game over");
        }

        return nextMove;
    }

    //undoes the last move and returns the new last move
    public Move undoLastMove() {
        mGameBoard.undoMove(mMoveHistory.remove(mMoveHistory.size() - 1));
        Move newLastMove = mMoveHistory.get(mMoveHistory.size() - 1);
        mGameBoard.undoMove(newLastMove);
        mIsPlayer1Turn = !mIsPlayer1Turn;
        mGameBoard.makeMove(newLastMove);
        return newLastMove;
    }

    public boolean checkIfAllowUndo() {
        return (mMoveHistory.size() > 1);
    }

    //Passes an array of int[] coordinates that direct to all games
    public List<int []> listAvailableGames(){
        if(mGameBoard.getNextGameY()==-1){
            return mGameBoard.listAvailableGames();
        }else {
            List<int[]> availableMoves = new ArrayList<int[]>();
            availableMoves.add(new int[]{mGameBoard.getNextGameX(),getGameBoard().getNextGameY()});
            return (availableMoves);
        }
    }

    //Passes the request on to the Board, as it is what can actually answer the question
    public List<int[]> listWonGames(){
        return mGameBoard.listAvailableGames();
    }

    public int getPlayerWithMostWins(){
        return mGameBoard.getPlayerWithMostWins();
    }

    public boolean isLastMoveGameWinning(){
        Move lastMove = getLastMove();
        return (mGameBoard.isSubGameWon(lastMove.getGameX(),lastMove.getGameY()));
    }

    public boolean isNextMoveAnyMove(){
        return (mGameBoard.getNextGameX() == -1);
    }

    public Move getLastMove(){
        return mMoveHistory.get(mMoveHistory.size()-1);
    }

    public void logBoard(Board board){
  /*      String output = "";
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
        }*/
    }

    public Board getGameBoard() {
        return mGameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.mGameBoard = gameBoard;
    }

    public boolean isPlayer1Turn() {
        return mIsPlayer1Turn;
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

