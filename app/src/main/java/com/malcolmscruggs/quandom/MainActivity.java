package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set default value for music
        //Source: http://freemusicarchive.org/music/Daniel_Birch/MUSIC_FOR_TV_FILM__GAMES_VOL1/The_Elevator_Game
        music = getIntent().getBooleanExtra("Music", false);
        Switch musicSwitch = findViewById(R.id.musicSwitch);
        musicSwitch.setChecked(music);

        Button quickPlay = findViewById(R.id.quickPlayButton);
        quickPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quickPlayIntent = new Intent(getApplicationContext(), PlayerSelectionActivity.class);
                quickPlayIntent.putExtra("Type", "quick");
                quickPlayIntent.putExtra("Music", music);
                startActivity(quickPlayIntent);
            }
        });

        Button customPlay = findViewById(R.id.customPlayButton);
        customPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent customPlayIntent = new Intent(getApplicationContext(), CustomPlayActivity.class);
                startActivity(customPlayIntent);
            }
        });

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                music = b;
                compoundButton.setChecked(music);
                toggleMusic(music);
            }
        });
    }

    private void toggleMusic(boolean music) {
        Intent musicIntent = new Intent(this, MusicService.class);
        musicIntent.putExtra("Music", music);
        if (music) {
            startService(musicIntent);
        } else {
            stopService(musicIntent);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
