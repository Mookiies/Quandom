package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private final static int TIMEOUT_DURATION = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        gameModel = (GameModel) getIntent().getSerializableExtra(MODEL_EXTRA_KEY);

        Switch musicSwitch = findViewById(R.id.musicSwitch);
        setupMusicSwitch(musicSwitch);

        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateQuestions(gameModel.getQuestions().size(),
                        gameModel.getCategory(), gameModel.getDifficulty(), true);
            }
        });

        Button changeCatButton = findViewById(R.id.changeCatButton);
        changeCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinActivity.this, CustomPlayActivity.class);
                intent.putExtra(MODEL_EXTRA_KEY, gameModel);
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

    private void populateQuestions(int numQuestions, final int category, final String difficulty, boolean mcq) {
        if (gameModel.isUsedCache()) {
            startIntent(readFromTextFile(R.raw.questions), category, difficulty, true);
        } else {
            RequestQueue queue = Volley.newRequestQueue(this);

            //Question Type
            String mcqOrTF;
            if (mcq) {
                mcqOrTF = "multiple";
            } else {
                mcqOrTF = "boolean";
            }

            String url;

            if (category == 8) {
                url = String.format("https://opentdb.com/api.php?amount=%d&difficulty=%s&type=multiple", numQuestions, difficulty.toLowerCase(), mcqOrTF);
            } else {
                url = String.format("https://opentdb.com/api.php?amount=%d&category=%d&difficulty=%s&type=multiple", numQuestions, category, difficulty.toLowerCase(), mcqOrTF);
            }

            Log.d("URL", url);

            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("APIResp", response);
                    //TODO handle when response doesn't contain necessary info
                    startIntent(response, category, difficulty, false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage = error.getMessage();
                    Log.d("APIResp", errorMessage != null ? errorMessage : "No error message");
                    Toast.makeText(WinActivity.this, getString(R.string.api_error), Toast.LENGTH_LONG).show();
                    startIntent(readFromTextFile(R.raw.questions), category, difficulty, true);
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_DURATION, 0, 0));
            queue.add(stringRequest);
        }
    }

    private void startIntent(String response, int category, String difficulty, boolean usedCache) {
        if (response == null) {
            Toast.makeText(this, getString(R.string.fetch_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(WinActivity.this, McqActivity.class);
        ArrayList<Player> gamePlayer = new ArrayList<>(gameModel.getPlayers());
        for (Player player : gamePlayer) {
            player.clearPlayerScore();
        }
        intent.putExtra(MODEL_EXTRA_KEY, new GameModel(gamePlayer, response, category, difficulty, usedCache));
        startActivity(intent);
    }

    private String readFromTextFile(int resID) {
        StringBuilder contents = new StringBuilder();
        try {
            InputStream is = getApplicationContext().getResources().openRawResource(resID);
            BufferedReader bs = new BufferedReader(new InputStreamReader(is));
            String tmp = null;
            while ((tmp = bs.readLine()) != null) {
                contents.append(tmp);
                contents.append("\n");
            }
            bs.close();
        } catch (IOException e) {
            Log.d("CacheQUES", e.getMessage());
            return null;
        }
        return contents.toString();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
