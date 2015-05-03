package olsonworks.UltimateTicTacToe;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    Board gameBoard;
    Boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Make official gameBoard

        final Button newGame = (Button) findViewById(R.id.new_game_button);
        final TextView moveCounter = (TextView) findViewById(R.id.move_counter);


        //Just a test by making random moves until game is over

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCounter.setText("HERE WE GOOOOOO!");
                gameBoard = new Board();
                gameOver = false;
            }
        };

        newGame.setOnClickListener(listener);

     /*   while (!gameOver)
        {
            makeRandomMove(gameBoard);
            logBoard(gameBoard);
            gameOver = gameBoard.checkWin();
        }

        Log.d("GAME MOVE:", "game over");
        */

    }

    //get random board move
    public void makeRandomMove(Board gameBoard) {
        //do i get to go anywhere?

        if (gameBoard.getNextGame() == -1) {
            gameBoard.makeMove((int) (Math.random() * 9), (int) (Math.random() * 9), gameBoard.getCurrentPlayer());
        } else {
            //or just in a specific game?
            gameBoard.makeMove(gameBoard.getNextGame(), (int) (Math.random() * 9), gameBoard.getCurrentPlayer());
        }
    }


    public void logBoard(Board board) {

        String output = "";
        int[][][][] boardSpace = board.indexBoard();

        Log.d("GAME MOVE:", "new move");
        Log.d("GAME MOVE:", "-------------");
        for (int iGameY = 0; iGameY < 3; iGameY++) {
            for (int iTileY = 0; iTileY < 3; iTileY++) {
                output = "|";
                for (int iGameX = 0; iGameX < 3; iGameX++) {
                    for (int iTileX = 0; iTileX < 3; iTileX++) {
                        switch (boardSpace[iGameX][iGameY][iTileX][iTileY]) {
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
                    output += "|";
                }
                Log.d("GAME MOVE:", output);
            }
            Log.d("GAME MOVE:", "-------------");
        }
    }
}
