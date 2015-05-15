package olsonworks.UltimateTicTacToe;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    private boolean firstMoveOrAny = true;
    private boolean mIsUndo = false;
    private boolean isAny = false;
    private String buttonText;
    public Move move = new Move();
    public GameController mainGame = new GameController();

    // Butterknife
    @InjectView(R.id.totalboard)
    TableLayout mGameTable;
    @InjectView(R.id.move_counter)
    TextView mMoveCounter;
    @InjectView(R.id.new_game_button)
    Button mNewGameButton;
    @InjectView(R.id.undo_button)
    Button mUndoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mUndoButton.setEnabled(false);
        setUpButtonsForOnClick();
    }

    // Ok, changed up how I coded this and set the "START A NEW GAME" button to call this method directly

    public void newGame(View view) {
        Animation animTranslate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate);
        Animation animAlpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
        mMoveCounter.setText("HERE WE GOOOOOO!");
        mUndoButton.setEnabled(false);
        mNewGameButton.startAnimation(animTranslate);
        mMoveCounter.startAnimation(animAlpha);
        firstMoveOrAny = true;
        mainGame = new GameController(0);
        move = new Move();
        SetBoardForNewOrWonGame(false);
        setUpButtonsForOnClick();
    }

    // This will undo 1 move right now

    public void undoMove(View view) {
        //   Need a method to be passed to check to see if history is one move
        if (mainGame.checkIfAllowUndo()) {
            TableRow R1 = (TableRow) mGameTable.getChildAt(move.getGameY());
            TableLayout T2 = (TableLayout) R1.getChildAt(move.getGameX());
            TableRow R2 = (TableRow) T2.getChildAt(move.getTileY());
            Button B = (Button) R2.getChildAt(move.getTileX());
            B.setText("");
            B.setEnabled(false);
            B.animate().rotationYBy(180).setDuration(300);
            disableOldSubgame(move.getTileX(), move.getTileY());
            enableNewSubgame(move.getGameX(), move.getGameY());
            move = mainGame.undoLastMove();
            mMoveCounter.setText(move.getTileX() + "," + move.getTileY() + "," + move.getGameX() + "," + move.getGameX() + "," + move.isPlayer1Turn());
        }
        else {
            mUndoButton.setEnabled(false);
            firstMoveOrAny = true;
            mainGame = new GameController(0);
            move = new Move();
            SetBoardForNewOrWonGame(false);
            setUpButtonsForOnClick();
        }
    }

    /* Set up these onClickListeners for alllll the gameboard buttons.   Basically it is:
    // Going TableLayout (whole game) -> TableRow (row of subgames)
     -> TableLayout (subgame) -> Table Row (row of buttons) -> Button */

    private void setUpButtonsForOnClick() {
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
                                    V.setOnClickListener(new makeMove(gameX, gameY, tileX, tileY));
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private class makeMove implements View.OnClickListener {

        private int tileX = 0;
        private int tileY = 0;
        private int gameX = 0;
        private int gameY = 0;

        // This grabs the coordinates.
        public makeMove(int tileX, int tileY, int gameX, int gameY) {
            this.tileX = tileX;
            this.tileY = tileY;
            this.gameX = gameX;
            this.gameY = gameY;
        }

        /* This puts the X and O on the button and displays the coordinates of the move.
         It changes the display to X or O depending on whose turn.   Also locks out the button.
        Finally, switches whose turn it is.   */

        @Override
        public void onClick(View view) {

            final Animation animScale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);

            if (view instanceof Button) {
                move.setTileX(tileX);
                move.setTileY(tileY);
                move.setGameX(gameX);
                move.setGameY(gameY);
                move.setPlayer1Turn(mainGame.isPlayer1Turn());

                // Grab the button and set it to O or X

                Button B = (Button) view;
                B.setText(mainGame.isPlayer1Turn() ? "X" : "O");
                B.startAnimation(animScale);
                B.animate().rotationYBy(180).setDuration(300);
                B.setEnabled(false);

                // Display the move
                mMoveCounter.setText(move.getTileX() + "," + move.getTileY() + "," + move.getGameX() + "," + move.getGameY() + "," + mainGame.isPlayer1Turn());
                mainGame.takeTurn(new Move(move));
                mUndoButton.setEnabled(true);

                if (mainGame.isLastMoveGameWinning()) {
                    setSubgameAsWon(gameX, gameY);
                }

                if (firstMoveOrAny) {
                    SetBoardForNewOrWonGame(true);
                    firstMoveOrAny = false;
                    enableNewSubgame(move.getTileX(), move.getTileY());
                } else if (mainGame.getIsGameOver()) {
                    SetBoardForNewOrWonGame(true);
                    String mWinnerString = (!mainGame.isPlayer1Turn() ? "X" : "O");
                    mMoveCounter.setText("GAME IS WON BY " + mWinnerString);
                } else if (mainGame.isNextMoveAnyMove()) {
                    firstMoveOrAny = true;
                    setButtonsforAny();
                    disableOldSubgame(move.getGameX(), move.getGameY());
                } else {
                    disableOldSubgame(move.getGameX(), move.getGameY());
                    enableNewSubgame(move.getTileX(), move.getTileY());
                }

            }
        }

        public void setSubgameAsWon(int gameX, int gameY) {
            TableRow R1 = (TableRow) mGameTable.getChildAt(gameY);
            TableLayout T2 = (TableLayout) R1.getChildAt(gameX);
            for (int y = 0; y < T2.getChildCount(); y++) {
                if (T2.getChildAt(y) instanceof TableRow) {
                    TableRow R2 = (TableRow) T2.getChildAt(y);
                    for (int x = 0; x < R2.getChildCount(); x++) {
                        if (R2.getChildAt(x) instanceof Button) {
                            Button B = (Button) R2.getChildAt(x);
                            B.setText(!mainGame.isPlayer1Turn() ? "X" : "O");
                            B.setEnabled(false);
                        }
                    }

                }
            }
        }
    }

    // This is sets all buttons to enabled (new game) or disabled (won game)

    private void SetBoardForNewOrWonGame(boolean wonGame) {
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
                                        if (wonGame) {
                                            B.setEnabled(false);
                                        } else {
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
    }

    private void setButtonsforAny() {
        List mAvailableGames = mainGame.listAvailableGames();
        int[] mGame;
        for (int i = 0; i < mAvailableGames.size(); i++) {
            mGame = (int[]) mAvailableGames.get(i);
            // for (int zz = 0; zz < mGameTable.getChildCount(); zz++) {
            if (mGameTable.getChildAt(mGame[1]) instanceof TableRow) {
                TableRow R1 = (TableRow) mGameTable.getChildAt(mGame[1]);
                // for (int z = 0; z < R1.getChildCount(); z++) {
                if (R1.getChildAt(mGame[0]) instanceof TableLayout) {
                    TableLayout T2 = (TableLayout) R1.getChildAt(mGame[0]);
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
            }
        }
    }


    public void disableOldSubgame(int gameX, int gameY) {
        TableRow R1 = (TableRow) mGameTable.getChildAt(gameY);
        TableLayout T2 = (TableLayout) R1.getChildAt(gameX);
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

    public void enableNewSubgame(int tileX, int tileY) {
        TableRow R1 = (TableRow) mGameTable.getChildAt(tileY);
        TableLayout T2 = (TableLayout) R1.getChildAt(tileX);
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
}

