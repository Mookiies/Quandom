package com.malcolmscruggs.quandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class CustomPlayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_play);

        Switch musicSwitch = findViewById(R.id.musicSwitch);
        setupMusicSwitch(musicSwitch);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup catsGroup = findViewById(R.id.catRadioGroup);
                int cat = -1;
                int i = 0;
                while (cat < 0 && i < catsGroup.getChildCount()) {
                    if (((RadioButton)catsGroup.getChildAt(i)).isChecked()) {
                        cat = i + 8;
                    }
                    i++;
                }
                if (cat == -1) {
                    Toast.makeText(CustomPlayActivity.this, "Choose a Category", Toast.LENGTH_SHORT).show();
                } else {
                    RadioGroup diffsGroup = findViewById(R.id.diffsGroup);
                    RadioButton checkedDiff = findViewById(diffsGroup.getCheckedRadioButtonId());
                    if (checkedDiff != null) {
                        String difficulty = checkedDiff.getTag().toString();
                        Intent intent = new Intent(CustomPlayActivity.this, PlayerSelectionActivity.class);
                        intent.putExtra("Type", "custom");
                        intent.putExtra("Category", cat);
                        intent.putExtra("Difficulty", difficulty);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CustomPlayActivity.this, "Choose a Difficulty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
