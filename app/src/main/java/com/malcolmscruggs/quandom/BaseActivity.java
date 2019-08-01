package com.malcolmscruggs.quandom;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean music;

    protected void setupMusicSwitch(Switch musicSwitch) {
        music = isMusicRunning();
        musicSwitch.setChecked(music);
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("SWITCH", "onCheckedChanged");
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

    private boolean isMusicRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (MusicService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
