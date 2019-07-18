package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    }

    private void setCurrentQuestion() {
        if (gameModel.isGameOver()) {
            Player winner = gameModel.getWinningPlayer();
            currentPlayerTextView.setText(winner.getPlayerName());
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
    }

    private void setPlayerScores() {
        List<Player> players = gameModel.getPlayers();
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        player1Score.setText(getString(R.string.player_score, p1.getPlayerName(), p1.getPlayerScore()));
        player2Score.setText(getString(R.string.player_score, p2.getPlayerName(), p2.getPlayerScore()));
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
                guess = -1;
        }

        boolean isCorrectGuess = gameModel.guessQuestion(guess);

        Log.d("ON GUESS", isCorrectGuess + "");

        if (isCorrectGuess) {
            setCurrentQuestion();
        } else {
            setCurrentPlayer();
        }

    }
}
