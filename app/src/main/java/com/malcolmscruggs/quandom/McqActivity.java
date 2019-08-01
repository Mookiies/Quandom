package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import utils.GameModel;
import utils.Player;
import utils.Question;

import static com.malcolmscruggs.quandom.WinActivity.WINNING_PLAYER_KEY;

public class McqActivity extends BaseActivity {

    public static final String MODEL_EXTRA_KEY = "QUESTIONS";
    private GameModel gameModel;

    TextView currentPlayerTextView;
    TextView questionTextView;
    TextView player1Score;
    TextView player2Score;
    TextView player3Score;
    TextView player4Score;
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);

        gameModel = (GameModel) getIntent().getSerializableExtra(MODEL_EXTRA_KEY);

        Switch musicSwitch = findViewById(R.id.musicSwitch);
        setupMusicSwitch(musicSwitch);

        currentPlayerTextView = findViewById(R.id.currentPlayerTextView);
        questionTextView = findViewById(R.id.mcqQuestionTextView);
        player1Score = findViewById(R.id.p1Score);
        player2Score = findViewById(R.id.p2Score);
        player3Score = findViewById(R.id.p3Score);
        player4Score = findViewById(R.id.p4Score);
        buttonA = findViewById(R.id.mcqAnswerAButton);
        buttonB = findViewById(R.id.mcqAnswerBButton);
        buttonC = findViewById(R.id.mcqAnswerCButton);
        buttonD = findViewById(R.id.mcqAnswerDButton);

        setCurrentQuestion();
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGuess(0);
            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGuess(1);
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGuess(2);
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGuess(3);
            }
        });

        List<Player> players = gameModel.getPlayers();
        if (players.size() > 1) {
            player2Score.setVisibility(View.VISIBLE);
        }
        if (players.size() > 2) {
            player3Score.setVisibility(View.VISIBLE);
        }
        if (players.size() > 3) {
            player3Score.setVisibility(View.VISIBLE);
        }

        player1Score.setText(players.get(0).getPlayerName() + ": 0");
        player1Score.setBackgroundColor(getResources().getColor(players.get(0).getPlayerColor(), getTheme()));

        switch (players.size()) {
            case 1:
                player2Score.setVisibility(View.GONE);
                player3Score.setVisibility(View.GONE);
                player4Score.setVisibility(View.GONE);
                break;
            case 2:
                player2Score.setText(players.get(1).getPlayerName() + ": 0");
                player2Score.setBackgroundColor(getResources().getColor(players.get(1).getPlayerColor(), getTheme()));
                player3Score.setVisibility(View.GONE);
                player4Score.setVisibility(View.GONE);
                break;
            case 3:
                player2Score.setText(players.get(1).getPlayerName() + ": 0");
                player3Score.setText(players.get(2).getPlayerName() + ": 0");
                player2Score.setBackgroundColor(getResources().getColor(players.get(1).getPlayerColor(), getTheme()));
                player3Score.setBackgroundColor(getResources().getColor(players.get(2).getPlayerColor(), getTheme()));
                player4Score.setVisibility(View.GONE);
                break;
            case 4:
                player2Score.setText(players.get(1).getPlayerName() + ": 0");
                player3Score.setText(players.get(2).getPlayerName() + ": 0");
                player4Score.setText(players.get(3).getPlayerName() + ": 0");
                player2Score.setBackgroundColor(getResources().getColor(players.get(1).getPlayerColor(), getTheme()));
                player3Score.setBackgroundColor(getResources().getColor(players.get(2).getPlayerColor(), getTheme()));
                player4Score.setBackgroundColor(getResources().getColor(players.get(3).getPlayerColor(), getTheme()));
                break;
        }
    }

    private void setCurrentQuestion() {
        if (gameModel.isGameOver()) {
            ArrayList<Player> winners = gameModel.getWinningPlayer();
            if (winners.size() == 1) {
                Player winner = winners.get(0);
                Intent intent = new Intent(getApplicationContext(), WinActivity.class);
                intent.putExtra("Type", "win");
                intent.putExtra(WINNING_PLAYER_KEY, winner.getPlayerName());
                startActivity(intent);
                return;
            } else {
                Intent intent = new Intent(getApplicationContext(), WinActivity.class);
                intent.putExtra("Type", "tie");

                ArrayList<String> winnerNames = new ArrayList<>();
                for (Player player : winners) {
                    winnerNames.add(player.getPlayerName());
                }
                intent.putExtra(WINNING_PLAYER_KEY, winnerNames);
                startActivity(intent);
                return;
            }
        }

        Question question = gameModel.getCurrentQuestion();
        ArrayList<String> answers = question.getAnswers();
        questionTextView.setText(question.getQuestionText());
        buttonA.setText(answers.get(0));
        buttonB.setText(answers.get(1));
        buttonC.setText(answers.get(2));
        buttonD.setText(answers.get(3));

        setCurrentPlayer();
        setPlayerScores();
    }

    private void setCurrentPlayer() {
        Player player = gameModel.getGuessingPlayer();
        currentPlayerTextView.setText(getString(R.string.currrent_player_turn, player.getPlayerName()));
        findViewById(R.id.currPlayerContainer).setBackgroundColor(getResources().getColor(player.getPlayerColor(), getTheme()));
    }

    private void setPlayerScores() {
        List<Player> players = gameModel.getPlayers();
        Player p1 = players.get(0);
        final Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        player1Score.startAnimation(bounce);
        player1Score.setText(p1.getPlayerName() + ": " + p1.getPlayerScore());

        if (players.size() > 1) {
            Player p2 = players.get(1);
            player2Score.startAnimation(bounce);
            player2Score.setText(p2.getPlayerName() + ": " + p2.getPlayerScore());
        }

        if (players.size() > 2) {
            Player p3 = players.get(2);
            player3Score.startAnimation(bounce);
            player3Score.setText(p3.getPlayerName() + ": " + p3.getPlayerScore());
        }

        if (players.size() > 3) {
            Player p4 = players.get(3);
            player4Score.startAnimation(bounce);
            player4Score.setText(p4.getPlayerName() + ": " + p4.getPlayerScore());
        }
    }

    private void onGuess(int guess) {
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