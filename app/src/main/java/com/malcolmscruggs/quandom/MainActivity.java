package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quickPlay = findViewById(R.id.quickPlayButton);
        quickPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quickPlayItent = new Intent(getApplicationContext(), PlayerSelectionActivity.class);
                quickPlayItent.putExtra("Type", "quick");
                startActivity(quickPlayItent);
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
