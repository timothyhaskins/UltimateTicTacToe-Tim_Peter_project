package olsonworks.UltimateTicTacToe;

import android.util.Log;

import java.util.List;

/**
 * Created by Peter Olson on 5/3/2015.
 */
public class AIPlayer {
    private boolean mIsPlayer1;
    private int mPlayerNumber;
    private int mAIType; //0=random

    public AIPlayer(boolean isPlayer1, int type){
        mIsPlayer1 = isPlayer1;
        mPlayerNumber = (mIsPlayer1 ? 1 : 2);
        mAIType = type;
    }

    public Move getMove (Board gameBoard){
        return getRandomMove(gameBoard);
    }

    private Move getRandomMove(Board gameBoard){
        List<Move> moves = gameBoard.listAvailableMoves();
        Move move = moves.get((int) (Math.random() * moves.size()));
        move.setPlayer1Turn(mIsPlayer1);
        return moves.get((int) (Math.random() * moves.size()));
    }

}
