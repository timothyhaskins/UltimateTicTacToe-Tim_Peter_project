package olsonworks.UltimateTicTacToe;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GameActivity extends ActionBarActivity {

    private boolean firstMove = true;
    private String buttonText;
    public Move move = new Move();
    public GameController mainGame = new GameController();
    public static List<gameList> gameViewList;

    // Butterknife
    @InjectView(R.id.totalboard)
    TableLayout mGameTable;
    @InjectView(R.id.move_counter)
    TextView mMoveCounter;
    @InjectView(R.id.new_game_button)
    Button mNewGameButton;
    @InjectView(R.id.undo_button)
    Button mUndoButton;
    @InjectView(R.id.game00)
    ImageView mGame00;
    @InjectView(R.id.game10)
    ImageView mGame01;
    @InjectView(R.id.game20)
    ImageView mGame02;
    @InjectView(R.id.game01)
    ImageView mGame10;
    @InjectView(R.id.game11)
    ImageView mGame11;
    @InjectView(R.id.game21)
    ImageView mGame12;
    @InjectView(R.id.game02)
    ImageView mGame20;
    @InjectView(R.id.game12)
    ImageView mGame21;
    @InjectView(R.id.game22)
    ImageView mGame22;
    @InjectView(R.id.playerID) ImageView mPlayerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);
        createGameViewList();
        mUndoButton.setEnabled(false);
        setUpButtonsForOnClick();
    }

    // Creates an ArrayList that links to all of the views in order to allow for
    // a "won square" graphic.

    public void createGameViewList() {
        gameViewList = new ArrayList<gameList>();
        gameViewList.add(new gameList(0, 0, mGame00));
        gameViewList.add(new gameList(1, 0, mGame10));
        gameViewList.add(new gameList(2, 0, mGame20));
        gameViewList.add(new gameList(0, 1, mGame01));
        gameViewList.add(new gameList(1, 1, mGame11));
        gameViewList.add(new gameList(2, 1, mGame21));
        gameViewList.add(new gameList(0, 2, mGame02));
        gameViewList.add(new gameList(1, 2, mGame12));
        gameViewList.add(new gameList(2, 2, mGame22));
    }

    // Resets the "won" graphics.

    public void resetGameViews() {
        for (int i = 0; i < gameViewList.size(); i++) {
            gameList list = gameViewList.get(i);
            list.getImageViewResource().setVisibility(View.INVISIBLE);
        }
    }

    // Starts with a fresh board.

    public void newGame(View view) {
        Animation animTranslate = AnimationUtils.loadAnimation(GameActivity.this, R.anim.translate);
        Animation animAlpha = AnimationUtils.loadAnimation(GameActivity.this, R.anim.alpha);
        mMoveCounter.setText("HERE WE GOOOOOO!");
        mUndoButton.setEnabled(false);
        mNewGameButton.startAnimation(animTranslate);
        mMoveCounter.startAnimation(animAlpha);
        firstMove = true;
        mainGame = new GameController(0);
        move = new Move();
        resetGameViews();
        setBoardForNewOrWonGame(false);
        setUpButtonsForOnClick();
    }

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
            setSubgameImageViewAsWon(move.getGameX(), move.getGameY(), false);
            disableOldSubgame(move.getTileX(), move.getTileY());
            enableNewSubgame(move.getGameX(), move.getGameY());
            move = mainGame.undoLastMove();
            if(!mainGame.isPlayer1Turn()) {
                mPlayerID.setImageResource(R.drawable.o);
            } else {
                mPlayerID.setImageResource(R.drawable.x);
            }
            mMoveCounter.setText(move.getTileX() + "," + move.getTileY() + "," + move.getGameX() + "," + move.getGameX());
        } else {
            mUndoButton.setEnabled(false);
            firstMove = true;
            mainGame = new GameController(0);
            move = new Move();
            setBoardForNewOrWonGame(false);
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

        private int tileX;
        private int tileY;
        private int gameX;
        private int gameY;

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

            final Animation animScale = AnimationUtils.loadAnimation(GameActivity.this, R.anim.scale);

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
                if(mainGame.isPlayer1Turn()) {
                    mPlayerID.setImageResource(R.drawable.o);
                } else {
                    mPlayerID.setImageResource(R.drawable.x);
                }
                mMoveCounter.setText(move.getTileX() + "," + move.getTileY() + "," + move.getGameX() + "," + move.getGameY() + "," + " Next turn: ");
                mainGame.takeTurn(new Move(move));
                mUndoButton.setEnabled(true);

                // Marks a game as won
                if (mainGame.isLastMoveGameWinning()) {
                    setSubgameImageViewAsWon(move.getGameX(), move.getGameY(), true);
                }
                if (mainGame.isNextMoveAnyMove()) {
                    setButtonsforAny();
                    firstMove = true;
                } else if (mainGame.getIsGameOver()) {
                    setBoardForNewOrWonGame(true);
                    String mWinnerString = (!mainGame.isPlayer1Turn() ? "X" : "O");
                    mMoveCounter.setText("GAME IS WON BY " + mWinnerString);
                } else if (firstMove) {
                    setBoardForNewOrWonGame(true);
                    firstMove = false;
                    enableNewSubgame(move.getTileX(), move.getTileY());
                } else {
                    disableOldSubgame(move.getGameX(), move.getGameY());
                    enableNewSubgame(move.getTileX(), move.getTileY());
                }
            }
        }
    }

    public void setSubgameImageViewAsWon(int gameX, int gameY, boolean won) {
        for (int i = 0; i < gameViewList.size(); i++) {
            gameList list = gameViewList.get(i);
            if (list.getGameX() == gameY && list.getGameY() == gameX) {
                list.getImageViewResource().setVisibility(View.VISIBLE);
                if (won) {
                    if (mainGame.isPlayer1Turn()) {
                        list.getImageViewResource().setImageResource(R.drawable.o);
                        list.getImageViewResource().animate().rotationYBy(180).setDuration(300);
                    } else {
                        list.getImageViewResource().setImageResource(R.drawable.x);
                        list.getImageViewResource().animate().rotationYBy(180).setDuration(300);
                    }
                } else {
                    list.getImageViewResource().setVisibility(View.INVISIBLE);
                }
            }
        }
        setSubgameButtonsAsWon(gameX, gameY, won);
    }

    public void setSubgameButtonsAsWon(int gameX, int gameY, boolean won) {
        TableRow R1 = (TableRow) mGameTable.getChildAt(gameY);
        TableLayout T2 = (TableLayout) R1.getChildAt(gameX);
        for (int y = 0; y < T2.getChildCount(); y++) {
            if (T2.getChildAt(y) instanceof TableRow) {
                TableRow R2 = (TableRow) T2.getChildAt(y);
                for (int x = 0; x < R2.getChildCount(); x++) {
                    if (R2.getChildAt(x) instanceof Button) {
                        Button B = (Button) R2.getChildAt(x);
                        if (won) {
                            B.setVisibility(View.INVISIBLE);
                            B.setEnabled(false);
                        } else {
                            B.setVisibility(View.VISIBLE);
                            if (B.getText() == "") {
                                B.setEnabled(true);
                            } else {
                                B.setEnabled(false);
                            }
                        }
                    }
                }
            }
        }
    }

    // This is sets all buttons to enabled (new game) or disabled (won game)
    private void setBoardForNewOrWonGame(boolean wonGame) {
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
                                            B.setVisibility(View.VISIBLE);
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
            if (mGameTable.getChildAt(mGame[1]) instanceof TableRow) {
                TableRow R1 = (TableRow) mGameTable.getChildAt(mGame[1]);
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
