package olsonworks.UltimateTicTacToe;

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

    //get random board move
    public Move makeRandomMove(Board gameBoard){
        List<Move> moves = gameBoard.listAvailableMoves();
        Move move = moves.get((int) (Math.random() * moves.size()));

        return move;
    }

}
