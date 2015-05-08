package olsonworks.UltimateTicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    TTTGame mainGame = new TTTGame();
    private boolean player1Turn = true;
    private boolean firstMove = true;
    private boolean isAny = false;
    private String buttonText;
    public Move move = new Move();

    // Butterknife
    @InjectView(R.id.totalboard) TableLayout mGameTable;
    @InjectView(R.id.move_counter) TextView mMoveCounter;


    // Temp variable for board
    public int[] tempGameMove = new int[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //Make official gameBoard
        final Button newGame = (Button) findViewById(R.id.new_game_button);


        // Make a new move

        // Testing out essentially a "New Game" button w/ a fresh board

        View.OnClickListener listener = new View.OnClickListener() {

            final Animation animTranslate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate);
            final Animation animAlpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);


            @Override
            public void onClick(View v) {
                mMoveCounter.setText("HERE WE GOOOOOO!");
                newGame.startAnimation(animTranslate);
                mMoveCounter.startAnimation(animAlpha);
                player1Turn = false;
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



    /* Set up these onClickListeners for alllll the gameboard buttons.   Basically it is:
    // Going TableLayout (whole game) -> TableRow (row of subgames)
     -> TableLayout (subgame) -> Table Row (row of buttons) -> Button */

    private void setupOnClickListeners() {
        for (int tileY = 0; tileY < mGameTable.getChildCount(); tileY++) {
            if (mGameTable.getChildAt(tileY) instanceof TableRow) {
                TableRow R1 = (TableRow) mGameTable.getChildAt(tileY);
                for (int tileX = 0; tileX < R1.getChildCount(); tileX++) {
                    if (R1.getChildAt(tileX) instanceof TableLayout) {
                        TableLayout T2 = (TableLayout) R1.getChildAt(tileX);
                        for (int gameY = 0; gameY < T2.getChildCount(); gameY++) {
                            if (T2.getChildAt(gameY) instanceof TableRow) {
                                TableRow R2 = (TableRow) T2.getChildAt(gameY);
                                for (int gameX = 0; gameX < R2.getChildCount(); gameX++) {
                                    View V = R2.getChildAt(gameX);
                                    V.setOnClickListener(new playOnClick(gameX, gameY, tileX, tileY));
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
        for (int zz = 0; zz < mGameTable.getChildCount(); zz++) {
            if (mGameTable.getChildAt(zz) instanceof TableRow) {
                TableRow R1 = (TableRow) mGameTable.getChildAt(zz);
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

        private int tileX = 0;
        private int tileY = 0;
        private int gameX = 0;
        private int gameY = 0;


        // This grabs the coordinates.

        public playOnClick(int tileX, int tileY, int gameX, int gameY) {
            this.tileX = tileX;
            this.tileY = tileY;
            this.gameX = gameX;
            this.gameY = gameY;
        }

        /* This puts the X and O on the button and displays the coordinates of the move.
         It changes the display to X or O depending on whose turn.   Also locks out the button.
        Finally, switches whose turn it is.   Of course, at some point it needs to lock out
         every table but the table currently being played in. */

        @Override
        public void onClick(View view) {

            final Animation animScale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);

            if (view instanceof Button) {
                tempGameMove[0] = tileX;
                tempGameMove[1] = tileY;
                tempGameMove[2] = gameX;
                tempGameMove[3] = gameY;
                move.setTileX(tileX);
                move.setTileY(tileY);
                move.setGameX(gameX);
                move.setGameY(gameY);
                move.setPlayer1Turn(player1Turn);

                // Starting to try to use an intent to pass the move to the game - not actually used yet
                // playGame(move);

                // Grab the button and set it to O or X

                Button B = (Button) view;
                B.setText(move.getPlayer1Turn() ? "O" : "X");

                // Two different ways to animate - old:
                B.startAnimation(animScale);
                // New:
                B.animate().rotationYBy(180).setDuration(300);

                // Disable the button
                B.setEnabled(false);

                // Display the move
                mMoveCounter.setText(move.mTileX + "," + move.mTileY + "," + move.mGameX + "," + move.mGameY);

                // Reverse the player
                player1Turn = !player1Turn;

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
    }


    // This would pass the move to the game via an intent.   Doesn't work.

    private void playGame(Move newMove) {
        Intent intent = new Intent(MainActivity.this, Move.class);
        intent.putExtra("newMove", (Parcelable) newMove);
        startActivity(intent);
    }

        public void disableOldSubgame() {
            TableRow R1 = (TableRow) mGameTable.getChildAt(move.getGameY());
            TableLayout T2 = (TableLayout) R1.getChildAt(move.getGameX());
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
            TableRow R1 = (TableRow) mGameTable.getChildAt(move.getTileY());
            TableLayout T2 = (TableLayout) R1.getChildAt(move.getTileX());
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
            for (int zz = 0; zz < mGameTable.getChildCount(); zz++) {
                if (mGameTable.getChildAt(zz) instanceof TableRow) {
                    TableRow R1 = (TableRow) mGameTable.getChildAt(zz);
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




