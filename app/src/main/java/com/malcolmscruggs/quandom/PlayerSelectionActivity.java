package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.ArrayList;

import utils.Player;
import utils.GameModel;

import static com.malcolmscruggs.quandom.McqActivity.MODEL_EXTRA_KEY;

public class PlayerSelectionActivity extends AppCompatActivity {

    int numPlayers;
    int numPoints;
    ArrayList<Player> players;
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

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
        players.add(new Player(getString(R.string.player1_placeholder), R.id.p1AvatarImg));
        players.add(new Player(getString(R.string.player2_placeholder), R.id.p2AvatarImg));
        players.add(new Player(getString(R.string.player3_placeholder), R.id.p3AvatarImg));

        // get player name editors and set up listeners
        final EditText playerNameChange1 = findViewById(R.id.p1NameText);
        final EditText playerNameChange2 = findViewById(R.id.p2NameText);
        final EditText playerNameChange3 = findViewById(R.id.p3NameText);

        setupPlayerEditText(0, playerNameChange1);
        setupPlayerEditText(1, playerNameChange2);
        setupPlayerEditText(2, playerNameChange3);

        // get button and set button listener
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerSelectionActivity.this, McqActivity.class);
                ArrayList<Player> gamePlayer = new ArrayList<>(players);
                if (numPlayers == 1) {
                    gamePlayer.remove(2);
                    gamePlayer.remove(1);
                } else if (numPlayers == 2) {
                    gamePlayer.remove(2);
                }
                intent.putExtra(MODEL_EXTRA_KEY, new GameModel(3, gamePlayer));
                startActivity(intent);
            }
        });
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

}
