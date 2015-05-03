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

    public Board gameBoard;
    public Boolean gameOver;
    private boolean whoTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Make official gameBoard

        final Button newGame = (Button) findViewById(R.id.new_game_button);
        final TextView moveCounter = (TextView) findViewById(R.id.move_counter);


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

        // This sets up the New Game button
        newGame.setOnClickListener(listener);
        // This sets up the rest.   Can they be merged?
        setupOnClickListeners();
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

    // This is one hell of a nested for loop, and you should be impressed, Peter.   It clears the board
    // for a new game.   See explanation of how it works below on the onclicklistener for the buttons.

    private void resetButtons() {

        // Grab the board
        TableLayout T = (TableLayout) findViewById(R.id.totalboard);
        for (int zz = 0; zz < T.getChildCount(); zz++) {
            if (T.getChildAt(zz) instanceof TableRow) {
                TableRow R1 = (TableRow) T.getChildAt(zz);
                for (int z = 0; z < R1.getChildCount(); z++) {
                    if (R1.getChildAt(z) instanceof TableLayout) {
                        TableLayout T2 = (TableLayout) R1.getChildAt(z);
                        for (int y = 0; y < T2.getChildCount(); y++) {
                            if (T2.getChildAt(y) instanceof TableRow) {
                                TableRow R2 = (TableRow) T2.getChildAt(y);
                                for (int x = 0; x < R2.getChildCount(); x++) {
                                    if (R2.getChildAt(x) instanceof Button) {
                                        Button B = (Button) R2.getChildAt(x);
                                        B.setText("");
                                        B.setEnabled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // Set up these onClickListeners for alllll the gameboard buttons.   Basically it is:
    // Going TableLayout (whole game) -> TableRow (row of subgames)
    // -> TableLayout (subgame) -> Table Row (row of buttons) -> Button

    private void setupOnClickListeners() {
        TableLayout T = (TableLayout) findViewById(R.id.totalboard);
        for (int zz = 0; zz < T.getChildCount(); zz++) {
            if (T.getChildAt(zz) instanceof TableRow) {
                TableRow R1 = (TableRow) T.getChildAt(zz);
                for (int z = 0; z < R1.getChildCount(); z++) {
                    if (R1.getChildAt(z) instanceof TableLayout) {
                        TableLayout T2 = (TableLayout) R1.getChildAt(z);
                        for (int y = 0; y < T2.getChildCount(); y++) {
                            if (T2.getChildAt(y) instanceof TableRow) {
                                TableRow R2 = (TableRow) T2.getChildAt(y);
                                for (int x = 0; x < R2.getChildCount(); x++) {
                                    View V = R2.getChildAt(x);
                                    V.setOnClickListener(new playOnClick(x, y, z, zz));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class playOnClick implements View.OnClickListener {

        private int x = 0;
        private int y = 0;
        private int z = 0;
        private int zz = 0;

        // This is declared twice.   Bad WET coding.
        TextView moveCounter = (TextView) findViewById(R.id.move_counter);

        // This grabs the coordinates.   Need to refactor above to x1 y1 x2 y2.

        public playOnClick(int x, int y, int z, int zz) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.zz = zz;
        }

        // This puts the X and O on the button and displays the coordinates of the move.
        // It changes the display to X or O depending on whose turn.   Also locks out the button.
        // Finally, switches whose turn it is.

        @Override
        public void onClick(View view) {

            if (view instanceof Button) {
                Button B = (Button) view;
                B.setText(whoTurn ? "O": "X");
                B.setEnabled(false);
                moveCounter.setText(x + "," + y + "," + z + "," + zz);
                whoTurn = !whoTurn;

            }
        }
    }


}



