package com.malcolmscruggs.quandom;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {
    MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Music started.", Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Music service created.", Toast.LENGTH_SHORT).show();
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Music service destroyed.", Toast.LENGTH_SHORT).show();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
