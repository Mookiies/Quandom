package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Log.d("MusicSERV", getLocalClassName()+" onCreate " );

        setContentView(R.layout.activity_main);

        //Set default value for music
        //Source: http://freemusicarchive.org/music/Daniel_Birch/MUSIC_FOR_TV_FILM__GAMES_VOL1/The_Elevator_Game
        Switch musicSwitch = findViewById(R.id.musicSwitch);
        setupMusicSwitch(musicSwitch);

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
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
