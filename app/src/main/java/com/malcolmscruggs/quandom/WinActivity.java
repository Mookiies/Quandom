package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;

import utils.GameModel;
import utils.Player;
import utils.Question;

import static com.malcolmscruggs.quandom.McqActivity.MODEL_EXTRA_KEY;

public class WinActivity extends BaseActivity {

    private GameModel gameModel;

    private LinearLayout summaryContainer;
    private LinearLayout leaderboardContainer;
    private LinearLayout leaderboardWinsContainer;
    private TextView winText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        gameModel = (GameModel) getIntent().getSerializableExtra(MODEL_EXTRA_KEY);

        Switch musicSwitch = findViewById(R.id.musicSwitch);
        setupMusicSwitch(musicSwitch);

        final Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                homeIntent.putExtra("Music", music);
                startActivity(homeIntent);
            }
        });

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Player> gamePlayer = new ArrayList<>(gameModel.getPlayers());
                for (Player player : gamePlayer) {
                    player.clearPlayerScore();
                }
                populateQuestions(gameModel.isUsedCache(), WinActivity.this, gamePlayer,
                        gameModel.getQuestions().size(), gameModel.getCategory(),
                        gameModel.getDifficulty(), true);
            }
        });

        Button changeCatButton = findViewById(R.id.changeCatButton);
        changeCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinActivity.this, CustomPlayActivity.class);
                intent.putExtra(MODEL_EXTRA_KEY, gameModel);
                intent.putExtra("Music", music);
                startActivity(intent);
            }
        });

        winText = findViewById(R.id.winText);
        setWinnerText();

        leaderboardContainer = findViewById(R.id.leaderboardContainer);
        initializeLeaderboard(gameModel.getSortedPlayers(), leaderboardContainer, "points");

        leaderboardWinsContainer = findViewById(R.id.leaderboardWinsContainer);
        initializeLeaderboard(gameModel.getSortedPlayersByWins(), leaderboardWinsContainer, "wins");

        summaryContainer = findViewById(R.id.summaryQuestionContainer);
        initializeSummary();

        if (music) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tada);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        }
    }

    private void initializeLeaderboard(ArrayList<Player> players, LinearLayout container, String type) {
        for (int i = players.size() - 1; i >= 0; i--) {
            Player player = players.get(i);
            View view = View.inflate(this, R.layout.leaderboard_item, null);
            TextView playerName = view.findViewById(R.id.leaderboardPlayerText);
            View playerColor = view.findViewById(R.id.leaderboardPlayerColor);
            if (type.equals("points")) {
                playerName.setText(getString(R.string.leaderbaord_player_points, player.getPlayerName(), player.getPlayerScore()));
            } else {
                playerName.setText(getString(R.string.leaderboard_player_wins, player.getPlayerName(), player.getPlayerWins()));
            }
            playerColor.setBackgroundColor(getResources().getColor(player.getPlayerColor(), getTheme()));
            container.addView(view);
        }
    }

    private void setWinnerText() {
        ArrayList<Player> winningPlayers = gameModel.getWinningPlayer();
        for (Player player : winningPlayers) {
            player.increasePlayerWinsOnce();
        }
        if (winningPlayers.size() == 1) {
            Player winner = winningPlayers.get(0);
            winText.setText(getString(R.string.win_callout, winner.getPlayerName()));
            winText.setBackgroundColor(getResources().getColor(winner.getPlayerColor(), getTheme()));
        } else {
            String winners = formatPlayers(winningPlayers, " ");
            winText.setText(getString(R.string.player_tie, winners));
        }
    }

    private void initializeSummary() {
        for (Question question : gameModel.getQuestions()) {
            View view = View.inflate(this, R.layout.question_summary_item, null);
            TextView questionText = view.findViewById(R.id.summaryQuestionText);
            TextView answerText = view.findViewById(R.id.summaryAnswerText);
            TextView correctGuessText = view.findViewById(R.id.summaryCorrectGuesserText);

            ArrayList<Player> correctPlayers = question.getCorrectGuessingPlayers();
            String correctPlayersText = formatPlayers(correctPlayers, ", ");
            questionText.setText(question.getQuestionText());
            Spanned answer = Html.fromHtml(getString(R.string.summary_answer, question.getCorrectAnswerString()));
            answerText.setText(answer);
            if (correctPlayersText == null) {
                correctGuessText.setText(R.string.summary_no_correct_answer);
                correctGuessText.setTextColor(getResources().getColor(R.color.textRed, getTheme()));
            } else {
                correctGuessText.setText(getString(R.string.summary_correct_players, correctPlayersText));
            }
            summaryContainer.addView(view);
        }
    }

    private String formatPlayers(ArrayList<Player> players, String formatter) {
        if (players.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(players.get(0).getPlayerName());
        for (int i = 1; i < players.size(); i++) {
            sb.append(formatter).append(players.get(i).getPlayerName());
        }
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.putExtra("Music", music);
        startActivity(backIntent);
    }
}
