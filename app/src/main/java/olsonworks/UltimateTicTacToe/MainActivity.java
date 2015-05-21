package olsonworks.UltimateTicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.button_1player) Button mOnePlayerButton;
    @InjectView(R.id.button_2player) Button mTwoPlayerButton;
    @InjectView(R.id.button_2playerint) Button mTwoPlayerInternetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mOnePlayerButton.setEnabled(false);
        mTwoPlayerInternetButton.setEnabled(false);
    }

    public void StartTwoPlayer(View view) {
        startStory();
    }

    private void startStory() {
        // Intent = THIS reference to activity, then ref to next activity
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        // Set up the variable that will be passed, using the key name
        // Pass the variable
        startActivity(intent);
    }
}

