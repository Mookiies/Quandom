package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
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

import static com.malcolmscruggs.quandom.McqActivity.MODEL_EXTRA_KEY;

public class CustomPlayActivity extends BaseActivity {

    GameModel model;
    private final static int TIMEOUT_DURATION = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_play);

        Switch musicSwitch = findViewById(R.id.musicSwitch);
        setupMusicSwitch(musicSwitch);

        model = (GameModel)getIntent().getSerializableExtra(MODEL_EXTRA_KEY);
        Button nextButton = findViewById(R.id.nextButton);

        if (model == null) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioGroup catsGroup = findViewById(R.id.catRadioGroup);
                    int cat = -1;
                    int i = 0;
                    while (cat < 0 && i < catsGroup.getChildCount()) {
                        if (((RadioButton)catsGroup.getChildAt(i)).isChecked()) {
                            cat = i + 8;
                        }
                        i++;
                    }
                    if (cat == -1) {
                        Toast.makeText(CustomPlayActivity.this, "Choose a Category", Toast.LENGTH_SHORT).show();
                    } else {
                        RadioGroup diffsGroup = findViewById(R.id.diffsGroup);
                        RadioButton checkedDiff = findViewById(diffsGroup.getCheckedRadioButtonId());
                        if (checkedDiff != null) {
                            String difficulty = checkedDiff.getTag().toString();
                            Intent intent = new Intent(CustomPlayActivity.this, PlayerSelectionActivity.class);
                            intent.putExtra("Type", "custom");
                            intent.putExtra("Category", cat);
                            intent.putExtra("Difficulty", difficulty);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CustomPlayActivity.this, "Choose a Difficulty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioGroup catsGroup = findViewById(R.id.catRadioGroup);
                    int cat = -1;
                    int i = 0;
                    while (cat < 0 && i < catsGroup.getChildCount()) {
                        if (((RadioButton)catsGroup.getChildAt(i)).isChecked()) {
                            cat = i + 8;
                        }
                        i++;
                    }
                    if (cat == -1) {
                        Toast.makeText(CustomPlayActivity.this, "Choose a Category", Toast.LENGTH_SHORT).show();
                    } else {
                        RadioGroup diffsGroup = findViewById(R.id.diffsGroup);
                        RadioButton checkedDiff = findViewById(diffsGroup.getCheckedRadioButtonId());
                        if (checkedDiff != null) {
                            String difficulty = checkedDiff.getTag().toString();
                            populateQuestions(model.getQuestions().size(), cat, difficulty, true);
                        } else {
                            Toast.makeText(CustomPlayActivity.this, "Choose a Difficulty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void populateQuestions(int numQuestions, final int category, final String difficulty, boolean mcq) {
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
                    Toast.makeText(CustomPlayActivity.this, getString(R.string.api_error), Toast.LENGTH_LONG).show();
                    startIntent(readFromTextFile(R.raw.questions), category, difficulty, true);
                }
            });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_DURATION, 0, 0));
        queue.add(stringRequest);
    }

    private void startIntent(String response, int category, String difficulty, boolean usedCache) {
        if (response == null) {
            Toast.makeText(this, getString(R.string.fetch_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(CustomPlayActivity.this, McqActivity.class);
        ArrayList<Player> gamePlayer = new ArrayList<>(model.getPlayers());

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
}
