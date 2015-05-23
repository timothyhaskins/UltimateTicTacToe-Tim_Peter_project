package olsonworks.UltimateTicTacToe;

import java.util.List;

/**
 * Created by Peter Olson on 5/3/2015.
 */
public class AIPlayer {
    private boolean mIsPlayer1;
    private int mPlayerNumber;
    private int mAIType; //0=random 1=stupid etc...

    public AIPlayer(boolean isPlayer1, int type){
        mIsPlayer1 = isPlayer1;
        mPlayerNumber = (mIsPlayer1 ? 1 : 2);
        mAIType = type;
    }

    public Move getNextMove(Board gameBoard){
        switch (mAIType) {
            case 1: return getRandomMove(gameBoard);
            default: return getRandomMove(gameBoard);
        }

    }

    //get random board move
    private Move getRandomMove(Board gameBoard){
        List<Move> moves = gameBoard.listAvailableMoves();
        Move move = moves.get((int) (Math.random() * moves.size()));

        return move;
    }

}
