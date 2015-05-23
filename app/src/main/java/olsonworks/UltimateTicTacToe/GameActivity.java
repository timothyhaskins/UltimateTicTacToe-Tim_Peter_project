package olsonworks.UltimateTicTacToe;

import android.content.Intent;
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
    private String mPlayer1Name;
    private String mPlayer2Name;
    private int mGameType;
    private int mAIType;
    private String mCurrentPlayerName;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        mGameType = intent.getIntExtra("gameType", 0);
        mAIType = intent.getIntExtra("aiType", 0);
        mPlayer1Name = intent.getStringExtra("player1Name");
        if (mAIType == 1) {
            mPlayer2Name = "Computer";
        } else {
            mPlayer2Name = intent.getStringExtra("player2Name");
        }
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
        mNewGameButton.startAnimation(animTranslate);
        mMoveCounter.startAnimation(animAlpha);
        firstMove = true;
        mUndoButton.setEnabled(false);
        mainGame = new GameController(mGameType,mAIType);
        move = new Move();
        resetGameViews();
        setBoardForNewOrWonGame(false);
        setUpButtonsForOnClick();
    }

    public void undoMove(View view) {
           for (int i = 0; i <= mGameType; i++) {
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
                move = mainGame.undoLastMove();
                mMoveCounter.setText(move.getTileX() + "," + move.getTileY() + "," + move.getGameX()
                        + "," + move.getGameY() + "," + " Next turn: " + mCurrentPlayerName);
            } else {
                mUndoButton.setEnabled(false);
                firstMove = true;
                mainGame = new GameController(mGameType, mAIType);
                move = new Move();
                setBoardForNewOrWonGame(false);
                setUpButtonsForOnClick();
            }
        }
    }

    // Set up these onClickListeners for alllll the gameboard buttons.
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
                if (!mainGame.isPlayer1Turn()) {
                    mCurrentPlayerName = mPlayer1Name;
                } else {
                    mCurrentPlayerName = mPlayer2Name;
                }
                B.startAnimation(animScale);
                B.animate().rotationYBy(180).setDuration(300);
                B.setEnabled(false);

                mMoveCounter.setText(move.getTileX() + "," + move.getTileY() + "," + move.getGameX()
                        + "," + move.getGameY() + "," + " Next turn: " + mCurrentPlayerName);
                mainGame.takeTurn(new Move(move));
                updateUI(move);

                if (mGameType == 1 && !mainGame.getIsGameOver()) {
                    move = mainGame.takeAITurn();
                    makeAIMove(move);
                }
            }
        }
    }

    public void updateUI(Move move) {
        // Marks a game as won
        if (mainGame.isLastMoveGameWinning()) {
            setSubgameImageViewAsWon(move.getGameX(), move.getGameY(), true);
        }
        if (mainGame.getIsGameOver()) {
            setBoardForNewOrWonGame(true);
            String mWinnerString = (!mainGame.isPlayer1Turn() ? "X" : "O");
            mMoveCounter.setText("GAME IS WON BY " + mWinnerString);
        } else if (mainGame.isNextMoveAnyMove()) {
            setButtonsforAny();
            firstMove = true;
        } else if (firstMove) {
            setBoardForNewOrWonGame(true);
            firstMove = false;
            mUndoButton.setEnabled(true);
            enableNewSubgame(move.getTileX(), move.getTileY());
        } else {
            disableOldSubgame(move.getGameX(), move.getGameY());
            enableNewSubgame(move.getTileX(), move.getTileY());
        }
    }

    public void makeAIMove(Move move){

        final Animation animScale = AnimationUtils.loadAnimation(GameActivity.this, R.anim.scale);

        // Button Bit
        TableRow R1 = (TableRow) mGameTable.getChildAt(move.getGameY());
        TableLayout T1 = (TableLayout) R1.getChildAt(move.getGameX());
        TableRow R2 = (TableRow) T1.getChildAt(move.getTileY());
        Button B = (Button) R2.getChildAt(move.getTileX());
        B.setText("O");
        B.startAnimation(animScale);
        B.animate().rotationYBy(180).setDuration(300).setStartDelay(100);
        B.setEnabled(false);
        updateUI(move);
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
