package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

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

        Button quickInfoButton = findViewById(R.id.quickInfoButton);
        quickInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popup = inflater.inflate(R.layout.info_popup, null);
                TextView popupTextView = popup.findViewById(R.id.popupTextView);
                popupTextView.setText(R.string.quick_play_info_text);
                final PopupWindow popupWindow = new PopupWindow(popup,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popup.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
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

        Button customInfoButton = findViewById(R.id.customInfoButton);
        customInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popup = inflater.inflate(R.layout.info_popup, null);
                TextView popupTextView = popup.findViewById(R.id.popupTextView);
                popupTextView.setText(R.string.custom_play_info_text);
                final PopupWindow popupWindow = new PopupWindow(popup,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popup.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
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
