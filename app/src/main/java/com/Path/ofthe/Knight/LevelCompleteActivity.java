package com.Path.ofthe.Knight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LevelCompleteActivity extends AppCompatActivity {

    private ImageView BackBtn,SettingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_complete);

        BackBtn = findViewById(R.id.BackBtn);
        SettingsBtn = findViewById(R.id.SettingsBtn);

        TextView levelCompleteText = findViewById(R.id.levelCompleteText);
        ImageView nextLevelButton = findViewById(R.id.nextLevelButton);
        Button mainMenuButton = findViewById(R.id.mainMenuButton);

        // Buttons Settings and Back
        SettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelCompleteActivity.this, Settings.class);
                startActivity(intent);

            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelCompleteActivity.this, LevelSelectActivity.class);
                startActivity(intent);
            }
        });

        // Get the current level from the intent
        Intent intent = getIntent();
        int currentLevel = intent.getIntExtra("LEVEL", 1);
        levelCompleteText.setText("Level " + currentLevel + " Complete!");



        // Proceed to the next level
        nextLevelButton.setOnClickListener(v -> {
            Intent nextLevelIntent = new Intent(LevelCompleteActivity.this, MainActivity.class);
            nextLevelIntent.putExtra("LEVEL", currentLevel + 1); // Go to the next level
            startActivity(nextLevelIntent);
            finish();
        });


        // Go back to the main menu
        mainMenuButton.setOnClickListener(v -> {
            Intent mainMenuIntent = new Intent(LevelCompleteActivity.this, LevelSelectActivity.class);
            startActivity(mainMenuIntent);
            finish();
        });
    }
}
