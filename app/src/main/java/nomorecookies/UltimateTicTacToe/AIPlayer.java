package nomorecookies.UltimateTicTacToe;

import android.util.Log;

import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter Olson on 5/3/2015.
 */
public class AIPlayer {
    private boolean mIsPlayer1;
    private int mPlayerNumber;
    private int mAIType; //0=random 1=stupid etc...
    private int[][] mTileValues;
    private int [][][] mWinRoutes;
    private Hashtable mTileAdvantageValues;
    private Random generator;

    public AIPlayer(boolean isPlayer1, int type){
        mIsPlayer1 = isPlayer1;
        mPlayerNumber = (mIsPlayer1 ? 1 : 2);
        mAIType = type;
        generator  = new Random(System.currentTimeMillis());

        mTileValues = new int[][]{{2,3,2},{3,4,3},{2,3,2}};
        mWinRoutes = new int[][][]{{{0,0},{0,1},{0,2}},{{1,0},{1,1},{1,2}},{{2,0},{2,1},{2,2}},{{0,0},{1,0},{2,0}},{{0,1},{1,1},{2,1}},{{0,2},{1,2},{2,2}},{{0,0},{1,1},{2,2}},{{0,2},{1,1},{2,1}}};
        mTileAdvantageValues = new Hashtable();
    }


    public Move getNextMove(Board gameBoard){
        generator.setSeed(System.currentTimeMillis());
        switch (mAIType) {
            case 0: return new Move(getRandomMove(gameBoard));
            case 1: return new Move(getBestMove(gameBoard,mPlayerNumber,1));
            default: return new Move(getBestMove(gameBoard,mPlayerNumber,mAIType));
        }
    }


    //Get random board move
    private Move getRandomMove(Board gameBoard){
        List<Move> moves = gameBoard.listAvailableMoves();
        Move move = moves.get(generator.nextInt(moves.size()));
        move.setPlayer1Turn(mIsPlayer1);
        return move;
    }


    //Finds the move with the highest win/loss potential based on moves left to win in each subGame
    private Move getBestMove(Board board, int playerNumber, int levelsDeepToInspect){
        List<Move> moves = board.listAvailableMoves();

        //Make each of the list of moves have this player as the .player
        for (int moveNum = 0; moveNum < moves.size(); moveNum++) {
            moves.get(moveNum).setPlayer1Turn(playerNumber == 1);
        }

        //Initialize the check for move with the highest value
        float bestMoveValue = getMoveValue(board,playerNumber,moves.get(0),levelsDeepToInspect);
        Move bestMove = moves.get(0);


        //get the value of each move, and record the move with the highest value to the player
        for (int i = 1; i < moves.size(); i++) {
            float moveValue = getMoveValue(board,playerNumber,moves.get(i),levelsDeepToInspect);
            if ((moveValue > bestMoveValue) || ((moveValue == bestMoveValue) && (generator.nextBoolean()))){
                bestMoveValue = moveValue;
                bestMove = moves.get(i);
            }
        }

        return  bestMove;
    }


    //As above, but it returns the value of the best move value, rather then the move itself.
    //Used to find the potential benefit to an opponent for a move
    private float getBestMoveValue(Board board, int playerNumber, int levelsDeepToInspect){
        List<Move> moves = board.listAvailableMoves();

        //Make each of the list of moves have this player as the .player
        for (int moveNum = 0; moveNum < moves.size(); moveNum++) {
            moves.get(moveNum).setPlayer1Turn(playerNumber == 1);
        }

        //Initialize the check for move with the highest value
        float bestMoveValue = getMoveValue(board, playerNumber, moves.get(0), levelsDeepToInspect);

        //get the value of each move, and record the move with the highest value to the player
        for (int i = 1; i < moves.size(); i++) {
            float moveValue = getMoveValue(board,playerNumber,moves.get(i),levelsDeepToInspect);
            if ((moveValue > bestMoveValue) || ((moveValue == bestMoveValue) && (generator.nextBoolean()))){
                bestMoveValue = moveValue;
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

        //returns 10000 if this would be a game winning move
        if(newBoard.checkWin()){
            return 1000000;
        }

        //get value of subGame if it would be won (default value of game x 10)
        if (newBoard.isSubGameWon(move.getGameX(), move.getGameY())){
            moveValue = getWinPotential(playerNumber,newBoard) * 1000;
        }else{
            //get change in win potential for square (number of ways to win)
            float newWinRatio = getNetWinLossPotential(playerNumber, newBoard.getSubGame(move.getGameX(), move.getGameY()));
            float oldWinRatio = getNetWinLossPotential(playerNumber, board.getSubGame(move.getGameX(), move.getGameY()));
            moveValue = mTileValues[move.getGameX()][move.getGameY()] * getWinPotential(playerNumber,newBoard) * (newWinRatio - oldWinRatio);
        }

        //deduct value of move due to aftermath of move
        if (levelsDeepToInspect>0) {
            moveValue = moveValue - getBestMoveValue(newBoard,(playerNumber==1?2:1),levelsDeepToInspect-1);
        }

        return moveValue;
    }


    //returns the win potential for the player/win potential for the opponent
    public float getWinLossPotentialRatio(int player, SubBoard subBoard){
        float winLossPotentialRatio;

        if (mTileAdvantageValues.containsKey(subBoard.getTileHash())) {
            //get WinLossPotentialRatio from the stored hashTable if it has already been calculated
            winLossPotentialRatio = (float)mTileAdvantageValues.get(subBoard.getTileHash());
            if (player == 1){
                winLossPotentialRatio = (winLossPotentialRatio/1);
                Log.d("GAME MOVE","Reading Hash: ");
            }
        }else{
            //get your own winPotential, then get the opponents WinPotential
            float targetPlayerPotential = getWinPotential(player, subBoard);
            float opponentPotential = getWinPotential((player == 1 ? 2 : 1), subBoard);

            //if the Opponent is incapable of winning this game, give yourself a 5, otherwise your Win/Loss Potential is your WinPotential/Opponent WinPotential
            if (opponentPotential == 0) {
                winLossPotentialRatio = 25;
            } else {
                winLossPotentialRatio = (targetPlayerPotential / opponentPotential);
            }

            //store winLossPotentialRatio to each of the 4 rotational versions of the subBoard in the HashTable
            int[] subBoardHashes = subBoard.getTileHashWithRotations();
            for (int rotationNum = 0; rotationNum < 4; rotationNum++) {
                mTileAdvantageValues.put(subBoardHashes[rotationNum],(player==1?(winLossPotentialRatio/1):(winLossPotentialRatio)));
            }
            Log.d("GAME MOVE","Storing Hash: ");
        }

        return winLossPotentialRatio;
    }

    //returns the win potential for the player/win potential for the opponent
    public float getNetWinLossPotential(int player, SubBoard subBoard){
        float winNetLossPotential;

        if (mTileAdvantageValues.containsKey(subBoard.getTileHash())) {
            //get WinLossPotentialRatio from the stored hashTable if it has already been calculated
            winNetLossPotential = (float)mTileAdvantageValues.get(subBoard.getTileHash());
            if (player == 1){
                winNetLossPotential = (winNetLossPotential * -1);
                Log.d("GAME MOVE","Reading Hash: ");
            }
        }else{
            //get your own winPotential, then get the opponents WinPotential
            float targetPlayerPotential = getWinPotential(player, subBoard);
            float opponentPotential = getWinPotential((player == 1 ? 2 : 1), subBoard);

            //your Win/Loss Potential is your WinPotential - Opponent WinPotential
            winNetLossPotential = targetPlayerPotential - opponentPotential;

            //store winNetLossPotential to each of the 4 rotational versions of the subBoard in the HashTable
            int[] subBoardHashes = subBoard.getTileHashWithRotations();
            for (int rotationNum = 0; rotationNum < 4; rotationNum++) {
                mTileAdvantageValues.put(subBoardHashes[rotationNum],(player==1?(winNetLossPotential * -1):(winNetLossPotential)));
            }
            Log.d("GAME MOVE","Storing Hash: ");
        }

        return winNetLossPotential;
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
