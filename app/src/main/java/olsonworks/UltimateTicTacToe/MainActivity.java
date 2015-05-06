package olsonworks.UltimateTicTacToe;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    public Board gameBoard;
    public Boolean gameOver;
    private boolean player2Turn = false;
    private boolean firstMove = true;
    private boolean isAny = false;
    private String buttonText;

    // Temp variable for board
    public int[] gameMove = new int[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Make official gameBoard
        final Button newGame = (Button) findViewById(R.id.new_game_button);
        final TextView moveCounter = (TextView) findViewById(R.id.move_counter);

        // Testing out essentially a "New Game" button w/ a fresh board

        View.OnClickListener listener = new View.OnClickListener() {

            final Animation animTranslate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate);
            final Animation animAlpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);

            @Override
            public void onClick(View v) {
                moveCounter.setText("HERE WE GOOOOOO!");
                newGame.startAnimation(animTranslate);
                moveCounter.startAnimation(animAlpha);
                gameBoard = new Board();
                gameOver = false;
                player2Turn = false;
                firstMove = true;
                resetAllButtons();
            }
        };

        // This sets up the New Game button
        // newGame.setOnClickListener(listener);
        // This sets up the rest.   Can they be merged?
        setupOnClickListeners();
        newGame.setOnClickListener(listener);
    }



     /*   while (!gameOver)
        {
            makeRandomMove(gameBoard);
            logBoard(gameBoard);
            gameOver = gameBoard.checkWin();
        }
        Log.d("GAME MOVE:", "game over");
        */

    /* get random board move
    public void makeRandomMove(Board gameBoard) {
        //do i get to go anywhere?

        if (gameBoard.getNextGame() == -1) {
            gameBoard.makeMove((int) (Math.random() * 9), (int) (Math.random() * 9), gameBoard.getCurrentPlayer());
        } else {
            //or just in a specific game?
            gameBoard.makeMove(gameBoard.getNextGame(), (int) (Math.random() * 9), gameBoard.getCurrentPlayer());
        }
    } */

    public void logBoard(Board board) {

        String output;
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


    /* Set up these onClickListeners for alllll the gameboard buttons.   Basically it is:
    // Going TableLayout (whole game) -> TableRow (row of subgames)
     -> TableLayout (subgame) -> Table Row (row of buttons) -> Button */

    private void setupOnClickListeners() {
        TableLayout T = (TableLayout) findViewById(R.id.totalboard);
        for (int y2 = 0; y2 < T.getChildCount(); y2++) {
            if (T.getChildAt(y2) instanceof TableRow) {
                TableRow R1 = (TableRow) T.getChildAt(y2);
                for (int x2 = 0; x2 < R1.getChildCount(); x2++) {
                    if (R1.getChildAt(x2) instanceof TableLayout) {
                        TableLayout T2 = (TableLayout) R1.getChildAt(x2);
                        for (int y1 = 0; y1 < T2.getChildCount(); y1++) {
                            if (T2.getChildAt(y1) instanceof TableRow) {
                                TableRow R2 = (TableRow) T2.getChildAt(y1);
                                for (int x1 = 0; x1 < R2.getChildCount(); x1++) {
                                    View V = R2.getChildAt(x1);
                                    V.setOnClickListener(new playOnClick(x1, y1, x2, y2));
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // This is one hell of a nested for loop, and you should be impressed, Peter.   It clears the board
    // for a new game.   See explanation of how it works below on the onclicklistener for the buttons.

    private void resetAllButtons() {
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
                                        if (!isAny) {
                                            B.setText("");
                                            B.setEnabled(true);
                                        }
                                        else {
                                            buttonText = B.getText().toString();
                                            if (buttonText.equals("X") || buttonText.equals("O")) {
                                                B.setEnabled(false);
                                            } else {
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
        }
    }

    private class playOnClick implements View.OnClickListener {

        private int x1 = 0;
        private int y1 = 0;
        private int x2 = 0;
        private int y2 = 0;
        private TextView moveCounter = (TextView) findViewById(R.id.move_counter);


        // This grabs the coordinates.

        public playOnClick(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }


        /* This puts the X and O on the button and displays the coordinates of the move.
         It changes the display to X or O depending on whose turn.   Also locks out the button.
        Finally, switches whose turn it is.   Of course, at some point it needs to lock out
         every table but the table currently being played in. */

        @Override
        public void onClick(View view) {

            final Animation animScale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);

            if (view instanceof Button) {
                gameMove[0] = x1;
                gameMove[1] = y1;
                gameMove[2] = x2;
                gameMove[3] = y2;
                Button B = (Button) view;
                B.setText(player2Turn ? "O" : "X");
                // Two different ways to animate - old:
                B.startAnimation(animScale);
                // New:
                B.animate().rotationYBy(180).setDuration(300);
                B.setEnabled(false);
                moveCounter.setText(x1 + "," + y1 + "," + x2 + "," + y2);
                player2Turn = !player2Turn;

                if (firstMove) {
                    disableBoardAfterAny();
                    firstMove = false;
                }
                disableOldSubgame();
                if (isAny) {
                    resetAllButtons();
                    firstMove = false;
                    isAny = false;
                }
                enableNewSubgame();
            }
        }




        public void disableOldSubgame() {
            TableLayout T = (TableLayout) findViewById(R.id.totalboard);
            TableRow R1 = (TableRow) T.getChildAt(gameMove[3]);
            TableLayout T2 = (TableLayout) R1.getChildAt(gameMove[2]);
            for (int y = 0; y < T2.getChildCount(); y++) {
                if (T2.getChildAt(y) instanceof TableRow) {
                    TableRow R2 = (TableRow) T2.getChildAt(y);
                    for (int x = 0; x < R2.getChildCount(); x++) {
                        if (R2.getChildAt(x) instanceof Button) {
                            Button B = (Button) R2.getChildAt(x);
                            B.setEnabled(false);
                        }
                    }
                }
            }
        }

        public void enableNewSubgame() {
            TableLayout T = (TableLayout) findViewById(R.id.totalboard);
            TableRow R1 = (TableRow) T.getChildAt(gameMove[1]);
            TableLayout T2 = (TableLayout) R1.getChildAt(gameMove[0]);
            for (int y = 0; y < T2.getChildCount(); y++) {
                if (T2.getChildAt(y) instanceof TableRow) {
                    TableRow R2 = (TableRow) T2.getChildAt(y);
                    for (int x = 0; x < R2.getChildCount(); x++) {
                        if (R2.getChildAt(x) instanceof Button) {
                            Button B = (Button) R2.getChildAt(x);
                            buttonText = B.getText().toString();
                            if (buttonText.equals("X") || buttonText.equals("O")) {
                                B.setEnabled(false);
                            } else {
                                B.setEnabled(true);
                            }
                        }
                    }
                }
            }
        }

        private void disableBoardAfterAny() {
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
                                            B.setEnabled(false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



