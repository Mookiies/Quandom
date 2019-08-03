package com.malcolmscruggs.quandom;

import android.content.Intent;
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
    TextView winText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        gameModel = (GameModel) getIntent().getSerializableExtra(MODEL_EXTRA_KEY);

        Switch musicSwitch = findViewById(R.id.musicSwitch);
        setupMusicSwitch(musicSwitch);

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        summaryContainer = findViewById(R.id.summaryQuestionContainer);
        initializeSummary();

        winText = findViewById(R.id.winText);
        setWinnerText();
    }

    private void setWinnerText() {
        ArrayList<Player> winningPlayers = gameModel.getWinningPlayer();
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
            String correctPlayersText = formatPlayers(correctPlayers, ", "); //TODO fix this
            questionText.setText(question.getQuestionText());
            Spanned answer = Html.fromHtml(getString(R.string.summary_answer, question.getCorrectAnswerString()));
            answerText.setText(answer);
            if (correctPlayersText == null) {
                correctGuessText.setVisibility(View.GONE);
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
        startActivity(new Intent(this, MainActivity.class));
    }
}
