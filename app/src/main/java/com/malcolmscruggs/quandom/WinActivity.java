package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import utils.Player;

public class WinActivity extends AppCompatActivity {

    public static final String WINNING_PLAYER_KEY = "WINNING_PLAYER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        TextView winText = findViewById(R.id.winText);

        if (getIntent().getStringExtra("Type").equals("win")) {
            String winningPlayer = getIntent().getStringExtra(WINNING_PLAYER_KEY);
            winText.setText(getString(R.string.win_callout, winningPlayer));
        } else if (getIntent().getStringExtra("Type").equals("tie")) {
            ArrayList<String> winners = getIntent().getStringArrayListExtra(WINNING_PLAYER_KEY);
            String winningPlayers = "";
            for (String name : winners) {
                winningPlayers += name + " ";
            }
            winningPlayers += "Tie!";
            winText.setText(winningPlayers);
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
