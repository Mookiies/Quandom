package com.malcolmscruggs.quandom;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean music;
    protected boolean useCache;
    private String cachedQuestions;
    protected Intent musicIntent;

    private final static int TIMEOUT_DURATION = 5000;

    protected void setupMusicSwitch(Switch musicSwitch) {
        music = getIntent().getBooleanExtra("Music", false);
        musicIntent = new Intent(this, MusicService.class);
        musicSwitch.setChecked(music);
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                music = b;
                compoundButton.setChecked(music);
                toggleMusic(music);
            }
        });
        cachedQuestions = readFromTextFile(R.raw.questions);
    }

    private void toggleMusic(boolean music) {
        musicIntent.putExtra("Music", music);
        if (music) {
            startService(musicIntent);
        } else {
            stopService(musicIntent);
        }
    }

    protected void populateQuestions(boolean usedCache, final Context context, final ArrayList<Player> players,
                                     final int numQuestions, final int category, final String difficulty, boolean mcq) {
        if (usedCache) {
            startIntent(players, context, cachedQuestions, category, difficulty, true, numQuestions);
        } else {
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
                    startIntent(players, context, response, category, difficulty, false, numQuestions);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage = error.getMessage();
                    Log.d("APIResp", errorMessage != null ? errorMessage : "No error message");
                    Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_LONG).show();
                    startIntent(players, context, cachedQuestions, category, difficulty, true, numQuestions);
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_DURATION, 0, 0));
            queue.add(stringRequest);
        }
    }

    private void startIntent(ArrayList<Player> players, Context context,
                             String response, int category, String difficulty, boolean usedCache, int numQuestions) {
        if (response == null) {
            Toast.makeText(this, getString(R.string.fetch_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, McqActivity.class);
        intent.putExtra(MODEL_EXTRA_KEY, new GameModel(players, response, category, difficulty, usedCache, numQuestions));
        intent.putExtra("Music", music);
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

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MusicSERV", getLocalClassName()+" onPause : "+music);
        stopService(musicIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MusicSERV", getLocalClassName()+" onResume : "+music);
        toggleMusic(music);
    }
}
