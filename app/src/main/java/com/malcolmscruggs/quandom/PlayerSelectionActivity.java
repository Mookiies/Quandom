package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.GameModel;
import utils.Player;

import static com.malcolmscruggs.quandom.McqActivity.MODEL_EXTRA_KEY;

public class PlayerSelectionActivity extends AppCompatActivity {

    int numPlayers;
    int numPoints;
    ArrayList<Player> players;
    Button playButton;
    ArrayList<Integer> colors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);

        // set default values for # of players and points
        numPlayers = 2;
        numPoints = 5;

        // get number pickers and set up values and listeners
        NumberPicker numPickerPlayers = findViewById(R.id.playersPicker);
        NumberPicker numPickerPoints = findViewById(R.id.pointsPicker);

        numPickerPlayers.setMinValue(1);
        numPickerPlayers.setMaxValue(3);
        numPickerPlayers.setValue(2);

        numPickerPoints.setMinValue(1);
        numPickerPoints.setMaxValue(10);
        numPickerPoints.setValue(5);

        numPickerPlayers.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                numPlayers = numberPicker.getValue();
                switch (numPlayers) {
                    case 1:
                        findViewById(R.id.p2Layout).setVisibility(View.GONE);
                        findViewById(R.id.p3Layout).setVisibility(View.GONE);
                        break;
                    case 2:
                        findViewById(R.id.p2Layout).setVisibility(View.VISIBLE);
                        findViewById(R.id.p3Layout).setVisibility(View.GONE);
                        break;
                    case 3:
                        findViewById(R.id.p2Layout).setVisibility(View.VISIBLE);
                        findViewById(R.id.p3Layout).setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        numPickerPoints.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                numPoints = numberPicker.getValue();
            }
        });

        // create Players
        players = new ArrayList<>(3);
        players.add(new Player(getString(R.string.player1_placeholder), Color.RED));
        players.add(new Player(getString(R.string.player2_placeholder), Color.GREEN));
        players.add(new Player(getString(R.string.player3_placeholder), Color.BLUE));

        // get player name editors and set up listeners
        final EditText playerNameChange1 = findViewById(R.id.p1NameText);
        final EditText playerNameChange2 = findViewById(R.id.p2NameText);
        final EditText playerNameChange3 = findViewById(R.id.p3NameText);

        setupPlayerEditText(0, playerNameChange1);
        setupPlayerEditText(1, playerNameChange2);
        setupPlayerEditText(2, playerNameChange3);

        // get player color editors and set up listeners
        final Button playerColor1 = findViewById(R.id.p1Color);
        final Button playerColor2 = findViewById(R.id.p2Color);
        final Button playerColor3 = findViewById(R.id.p3Color);

        setupPlayerColor(0, playerColor1);
        setupPlayerColor(1, playerColor2);
        setupPlayerColor(2, playerColor3);

        // get button and set button listener
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateQuestions(numPoints, 9, 1, true);
            }
        });
    }

    private void populateQuestions(int numQuestions, int category, int difficulty, boolean mcq) {
        RequestQueue queue = Volley.newRequestQueue(this);

        //Categories
        List<String> cats = Arrays.asList("general-knowledge");
        ArrayList<String> categories = new ArrayList<>();
        categories.addAll(cats);

        //Difficulty
        List<String> dif = Arrays.asList("easy", "medium", "hard");
        ArrayList<String> difficulties = new ArrayList<>();
        difficulties.addAll(dif);

        //Question Type
        String mcqOrTF;
        if (mcq) {
            mcqOrTF = "multiple";
        } else {
            mcqOrTF = "boolean";
        }
        String url = String.format("https://opentdb.com/api.php?amount=%d&category=%d&difficulty=%s&type=multiple", numQuestions, category, difficulties.get(difficulty), mcqOrTF);

        final StringRequest stringRquest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("APIResp", response);
                startIntent(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APIResp", error.getMessage());
            }
        });
        queue.add(stringRquest);
    }

    private void setupPlayerEditText(final int playerIdx, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                players.get(playerIdx).setPlayerName(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void setupPlayerColor(final int playerIdx, final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int background = ((ColorDrawable)button.getBackground()).getColor();
                int index = colors.indexOf(background) + 1;
                Log.d("index0", Integer.toString(index));
                if (index >= colors.size()) {
                    index = 0;
                }
                Log.d("index1", Integer.toString(index));
                for (int i = 0; i < numPlayers; i++) {
                    if (index < colors.size()) {
                        if (players.get(i).getPlayerColor() == colors.get(index)) {
                            index++;
                            Log.d("index2", Integer.toString(index));
                        }
                    }
                }
                Log.d("index3", Integer.toString(index));
                if (index >= colors.size()) {
                    index = 0;
                }
                Log.d("index4", Integer.toString(index));
                int newBackground = colors.get(index);
                players.get(playerIdx).setPlayerColor(newBackground);
                button.setBackgroundColor(newBackground);
            }
        });
    }

    private void startIntent(String response) {
        Intent intent = new Intent(PlayerSelectionActivity.this, McqActivity.class);
        ArrayList<Player> gamePlayer = new ArrayList<>(players);
        if (numPlayers == 1) {
            gamePlayer.remove(2);
            gamePlayer.remove(1);
        } else if (numPlayers == 2) {
            gamePlayer.remove(2);
        }
        intent.putExtra(MODEL_EXTRA_KEY, new GameModel(3, gamePlayer, response));
        startActivity(intent);
    }
}
