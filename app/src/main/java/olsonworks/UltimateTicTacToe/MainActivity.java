package olsonworks.UltimateTicTacToe;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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
        final Button xy00 = (Button) findViewById(R.id.button_0_0);

        // Testing out essentially a "New Game" button w/ a fresh board

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCounter.setText("HERE WE GOOOOOO!");
                gameBoard = new Board();
                gameOver = false;
                resetButtons();
            }
        };

        newGame.setOnClickListener(listener);

        View.OnClickListener playOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xy00.setText("X");

            }
        };

        // Testing out a VERY rudimentary onClickListener for when a button is pressed.
        // Need to find a genius way to automatically set up 64(!!!) of these

        xy00.setOnClickListener(playOnClick);

    }



     /*   while (!gameOver)
        {
            makeRandomMove(gameBoard);
            logBoard(gameBoard);
            gameOver = gameBoard.checkWin();
        }

        Log.d("GAME MOVE:", "game over");
        */



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

    // This should reset the board eventually.   Right now it will just reset ONE game board.
    // Now, I could very clumsily make this do all of the boards, but need an auto way.
    // You'd think I'd know how since the below gets a whole game board, but I stole this code.

    private void resetButtons() {
        TableLayout T = (TableLayout) findViewById(R.id.game_board_0);
        for (int y = 0; y < T.getChildCount(); y++) {
            if (T.getChildAt(y) instanceof TableRow) {
                TableRow R = (TableRow) T.getChildAt(y);
                for (int x = 0; x < R.getChildCount(); x++) {
                    if (R.getChildAt(x) instanceof Button) {
                        Button B = (Button) R.getChildAt(x);
                        B.setText("");
                        B.setEnabled(true);
                    }
                }
            }
        }

    }

}



