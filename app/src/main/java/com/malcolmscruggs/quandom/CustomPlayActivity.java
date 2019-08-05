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
                            ArrayList<Player> gamePlayer = new ArrayList<>(model.getPlayers());
                            for (Player player : gamePlayer) {
                                player.clearPlayerScore();
                            }

                            populateQuestions(CustomPlayActivity.this, gamePlayer,
                                    model.getQuestions().size(), cat, difficulty, true);
                        } else {
                            Toast.makeText(CustomPlayActivity.this, "Choose a Difficulty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
