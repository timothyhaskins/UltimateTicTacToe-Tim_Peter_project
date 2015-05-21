package olsonworks.UltimateTicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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
        mOnePlayerButton.setEnabled(false);
        mTwoPlayerInternetButton.setEnabled(false);


    }

    public void StartTwoPlayer(View view) {
        String player1Name = mPlayer1Name.getText().toString();
        String player2Name = mPlayer2Name.getText().toString();
        startStory(player1Name, player2Name);
    }

    private void startStory(String player1name, String player2name) {
        // Intent = THIS reference to activity, then ref to next activity
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        // Set up the variable that will be passed, using the key name
        intent.putExtra("player1name", player1name);
        intent.putExtra("player2name", player2name);
        // Pass the variable
        startActivity(intent);
    }
}

