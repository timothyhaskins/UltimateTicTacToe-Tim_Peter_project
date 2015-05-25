package olsonworks.UltimateTicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.button_1player) Button mOnePlayerButton;
    @InjectView(R.id.button_2player) Button mTwoPlayerButton;
    @InjectView(R.id.button_2playerint) Button mTwoPlayerInternetButton;
    @InjectView(R.id.player1name) EditText mPlayer1Name;
    @InjectView(R.id.player2name) EditText mPlayer2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Animation animTranslate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate);
        Animation animAlpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
        mOnePlayerButton.startAnimation(animTranslate);
        mTwoPlayerButton.startAnimation(animTranslate);
        mTwoPlayerInternetButton.startAnimation(animTranslate);
        mTwoPlayerInternetButton.setEnabled(false);
    }

    //the value used below sets the number of turns ahead the AI will look. Anything greater than 2 and it starts to lag between moves
    public void StartOnePlayer(View view) {
        String player1Name = mPlayer1Name.getText().toString();
        startStory(player1Name, "", 1, 3);
    }

    public void StartTwoPlayer(View view) {
        String player1Name = mPlayer1Name.getText().toString();
        String player2Name = mPlayer2Name.getText().toString();
        startStory(player1Name, player2Name, 0, 0);
    }

    private void startStory(String player1Name, String player2Name, int gameType, int aiType) {
        // Intent = THIS reference to activity, then ref to next activity
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        // Set up the variable that will be passed, using the key name
        intent.putExtra("player1Name", player1Name);
        intent.putExtra("player2Name", player2Name);
        intent.putExtra("gameType", gameType);
        intent.putExtra("aiType", aiType);
        // Pass the variable
        startActivity(intent);
    }
}

