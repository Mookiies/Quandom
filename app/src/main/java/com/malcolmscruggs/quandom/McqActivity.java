package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import utils.GameModel;
import utils.Player;
import utils.Question;

import static com.malcolmscruggs.quandom.WinActivity.WINNING_PLAYER_KEY;

public class McqActivity extends AppCompatActivity {

    public static final String MODEL_EXTRA_KEY = "QUESTIONS";
    private GameModel gameModel;

    TextView currentPlayerTextView;
    TextView questionTextView;
    TextView player1Score;
    TextView player2Score;
    TextView player3Score;
    RadioGroup mcqRadioGroup;
    RadioButton radioButtonA;
    RadioButton radioButtonB;
    RadioButton radioButtonC;
    RadioButton radioButtonD;
    Button guessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);

        gameModel = (GameModel) getIntent().getSerializableExtra(MODEL_EXTRA_KEY);
        Log.d("GAMEMODEL", gameModel.toString());

        currentPlayerTextView = findViewById(R.id.currentPlayerTextView);
        questionTextView = findViewById(R.id.mcqQuestionTextView);
        player1Score = findViewById(R.id.player1Score);
        player2Score = findViewById(R.id.player2Score);
        player3Score = findViewById(R.id.player3Score);
        mcqRadioGroup = findViewById(R.id.mcqAnswerRadioGroup);
        radioButtonA = findViewById(R.id.mcqAnswerAButton);
        radioButtonB = findViewById(R.id.mcqAnswerBButton);
        radioButtonC = findViewById(R.id.mcqAnswerCButton);
        radioButtonD = findViewById(R.id.mcqAnswerDButton);
        guessButton = findViewById(R.id.mcqGuessButton);

        setCurrentQuestion();
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGuess();
            }
        });

        List<Player> players = gameModel.getPlayers();
        if (players.size() > 1) {
            player2Score.setVisibility(View.VISIBLE);
        }
        if (players.size() > 2) {
            player3Score.setVisibility(View.VISIBLE);
        }
    }

    private void setCurrentQuestion() {
        if (gameModel.isGameOver()) {
            Player winner = gameModel.getWinningPlayer();
            Intent intent = new Intent(getApplicationContext(), WinActivity.class);
            intent.putExtra(WINNING_PLAYER_KEY, winner.getPlayerName());
            startActivity(intent);
            return;
        }

        Question question = gameModel.getCurrentQuestion();
        ArrayList<String> answers = question.getAnswers();
        questionTextView.setText(question.getQuestionText());
        radioButtonA.setText(answers.get(0));
        radioButtonB.setText(answers.get(1));
        radioButtonC.setText(answers.get(2));
        radioButtonD.setText(answers.get(3));

        setCurrentPlayer();
        setPlayerScores();
    }

    private void setCurrentPlayer() {
        Player player = gameModel.getGuessingPlayer();
        currentPlayerTextView.setText(getString(R.string.currrent_player_turn, player.getPlayerName()));
        findViewById(R.id.mcqView).setBackgroundColor(player.getPlayerColor());
    }

    private void setPlayerScores() {
        List<Player> players = gameModel.getPlayers();
        Player p1 = players.get(0);
        player1Score.setText(getString(R.string.player_score, p1.getPlayerName(), p1.getPlayerScore()));

        if (players.size() > 1) {
            Player p2 = players.get(1);
            player2Score.setText(getString(R.string.player_score, p2.getPlayerName(), p2.getPlayerScore()));
        }

        if (players.size() > 2) {
            Player p3 = players.get(2);
            player3Score.setText(getString(R.string.player_score, p3.getPlayerName(), p3.getPlayerScore()));
        }
    }

    private void onGuess() {
        int guess;
        switch (mcqRadioGroup.getCheckedRadioButtonId()) {
            case R.id.mcqAnswerAButton:
                guess = 0;
                break;
            case R.id.mcqAnswerBButton:
                guess = 1;
                break;
            case R.id.mcqAnswerCButton:
                guess = 2;
                break;
            case R.id.mcqAnswerDButton:
                guess = 3;
                break;
            default:
                return;
        }

        boolean haveAllPlayersGuessed = gameModel.guessQuestion(guess);

        Log.d("ON GUESS", haveAllPlayersGuessed + "");

        if (haveAllPlayersGuessed) {
            setCurrentQuestion();
        } else {
            setCurrentPlayer();
        }

    }

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_button_quit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}