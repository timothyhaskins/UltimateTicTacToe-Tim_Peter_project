package olsonworks.UltimateTicTacToe;

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

}
