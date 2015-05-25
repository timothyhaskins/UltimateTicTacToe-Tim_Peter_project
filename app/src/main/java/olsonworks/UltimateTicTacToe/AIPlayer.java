package olsonworks.UltimateTicTacToe;

import java.util.List;

/**
 * Created by Peter Olson on 5/3/2015.
 */
public class AIPlayer {
    private boolean mIsPlayer1;
    private int mPlayerNumber;
    private int mAIType; //0=random 1=stupid etc...
    private int[][] mTileValues;
    private int [][][] mWinRoutes;

    public AIPlayer(boolean isPlayer1, int type){
        mIsPlayer1 = isPlayer1;
        mPlayerNumber = (mIsPlayer1 ? 1 : 2);
        mAIType = type;

        mTileValues = new int[][]{{2,3,2},{3,4,3},{2,3,2}};
        mWinRoutes = new int[][][]{{{0,0},{0,1},{0,2}},{{1,0},{1,1},{1,2}},{{2,0},{2,1},{2,2}},{{0,0},{1,0},{2,0}},{{0,1},{1,1},{2,1}},{{0,2},{1,2},{2,2}},{{0,0},{1,1},{2,2}},{{0,2},{1,1},{2,1}}};
    }

    public Move getNextMove(Board gameBoard){
        switch (mAIType) {
            case 0: return new Move(getRandomMove(gameBoard));
            case 1: return new Move(getBestMove(gameBoard,mPlayerNumber,1));
            default: return new Move(getBestMove(gameBoard,mPlayerNumber,mAIType));
        }

    }

    //get random board move
    private Move getRandomMove(Board gameBoard){
        List<Move> moves = gameBoard.listAvailableMoves();
        Move move = moves.get((int) (Math.random() * moves.size()));
        move.setPlayer1Turn(mIsPlayer1);
        return move;
    }

    //finds the move with the highest win/loss potental based on moves left to win in each subGame
    private Move getBestMove(Board board, int playerNumber, int levelsDeepToInspect){
        List<Move> moves = board.listAvailableMoves();
        for (int moveNum = 0; moveNum < moves.size(); moveNum++) {
            moves.get(moveNum).setPlayer1Turn(playerNumber == 1);
        }
        float bestMoveValue = getMoveValue(board,playerNumber,moves.get(0),levelsDeepToInspect);
        Move bestMove = moves.get(0);

        for (int i = 1; i < moves.size(); i++) {
            float moveValue = getMoveValue(board,playerNumber,moves.get(i),levelsDeepToInspect);
            if ((moveValue > bestMoveValue) || ((moveValue == bestMoveValue) && (Math.random() >= .5))){
                bestMoveValue = moveValue;
                bestMove = moves.get(i);
            }
        }

        return  bestMove;
    }

    //As above, but it returns the value of the best move, rather then the move itself.
    //Used to find the potential benefit to an opponent for a move
    private float getBestMoveValue(Board board, int playerNumber, int levelsDeepToInspect){
        List<Move> moves = board.listAvailableMoves();
        for (int moveNum = 0; moveNum < moves.size(); moveNum++) {
            moves.get(moveNum).setPlayer1Turn(playerNumber == 1);
        }
        float bestMoveValue = getMoveValue(board,playerNumber,moves.get(0),levelsDeepToInspect);
        Move bestMove = moves.get(0);

        for (int i = 1; i < moves.size(); i++) {
            float moveValue = getMoveValue(board,playerNumber,moves.get(i),levelsDeepToInspect);
            if ((moveValue > bestMoveValue) || ((moveValue == bestMoveValue) && (Math.random() >= .5))){
                bestMoveValue = moveValue;
                bestMove = moves.get(i);
            }
        }

        return  bestMoveValue;
    }

    //returns the value of a move in terms of the ratio of moves needed to win for you/opponent
    private float getMoveValue(Board board, int playerNumber, Move move, int levelsDeepToInspect){
        float moveValue = 0;

        //make copy of board as if move was made
        Board newBoard = new Board(board);
        newBoard.makeMove(move);

        //returns 100 if this would be a game winning move
        if(newBoard.checkWin()) return 100;

        //get value of subGame if it would be won (default value of game x 10)
        if (newBoard.isSubGameWon(move.getGameX(), move.getGameY())){
            moveValue = getWinPotential(playerNumber,newBoard) * 10;
        }else{
            //get change in win potential for square (number of ways to win)
            float newWinRatio = getWinLossPotentialRatio(playerNumber, newBoard.getSubGame(move.getGameX(),move.getGameY()));
            float oldWinRatio = getWinLossPotentialRatio(playerNumber, board.getSubGame(move.getGameX(),move.getGameY()));
            moveValue = getWinPotential(playerNumber,newBoard) * (newWinRatio - oldWinRatio);
        }

        //deduct value of move due to aftermath of move
        if (levelsDeepToInspect>0) {
            moveValue = moveValue / getBestMoveValue(newBoard,(playerNumber==1?2:1),levelsDeepToInspect-1);
        }

        return moveValue;
    }

    //returns the win potential for the player/win potential for the opponent
    public float getWinLossPotentialRatio(int player, SubBoard subBoard){
        float targetPlayerPotential = getWinPotential(player,subBoard);
        float opponentPotential = getWinPotential((player == 1 ? 2 : 1),subBoard);

        if (opponentPotential == 0) {
            return 5;
        }else {
            return targetPlayerPotential / opponentPotential;
        }
    }

    //returns the win potential for the player in questions ((1 move to wins) + (2 move to wins)/2 + (3 move to wins)/3)
    public float getWinPotential(int player, SubBoard subBoard){
        float winPotential = 0;

        for (int winRouteNum = 0; winRouteNum < mWinRoutes.length; winRouteNum++) {
            float movesToWinRoute = 3;
            for (int tileNum = 0; tileNum < mWinRoutes[winRouteNum].length; tileNum++) {
                if (subBoard.getTile(mWinRoutes[winRouteNum][tileNum][0],mWinRoutes[winRouteNum][tileNum][1]) == player){
                    movesToWinRoute--;
                }else if (subBoard.getTile(mWinRoutes[winRouteNum][tileNum][0],mWinRoutes[winRouteNum][tileNum][1]) != 0){
                    movesToWinRoute = 0;
                    break;
                }
            }
            if (movesToWinRoute!=0) {
                winPotential += (1/movesToWinRoute);
            }
        }

        return winPotential;
    }

    public float getWinPotential(int player, Board Board){
        float winPotential = 0;

        for (int winRouteNum = 0; winRouteNum < mWinRoutes.length; winRouteNum++) {
            float movesToWinRoute = 3;
            for (int tileNum = 0; tileNum < mWinRoutes[winRouteNum].length; tileNum++) {
                if (Board.getSubGame(mWinRoutes[winRouteNum][tileNum][0],mWinRoutes[winRouteNum][tileNum][1]).getWinner() == player){
                    movesToWinRoute--;
                }else if (Board.getSubGame(mWinRoutes[winRouteNum][tileNum][0],mWinRoutes[winRouteNum][tileNum][1]).getWinner() != 0) {
                    movesToWinRoute = 0;
                    break;
                }
            }
            if (movesToWinRoute!=0) {
                winPotential += (1/movesToWinRoute);
            }
        }

        return winPotential;
    }

}
