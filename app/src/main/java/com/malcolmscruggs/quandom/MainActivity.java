package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void quickPlayPress(View view) {
        Button quickPlay = (Button) findViewById(R.id.quickPlayButton);
        quickPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quickPlayItent = new Intent(getApplicationContext(), McqActivity.class);
                startActivity(quickPlayItent);
            }
        });
    }
}
