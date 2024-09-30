package com.Path.ofthe.Knight;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LevelSelectActivity extends AppCompatActivity {

    ImageView BackBtn;

    private GridLayout levelGrid;
    private final int totalLevels = 14; // Total number of levels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        BackBtn = findViewById(R.id.BackBtn);

        levelGrid = findViewById(R.id.levelGrid);


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelSelectActivity.this, Menu.class);
                startActivity(intent);
            }
        });

        // Dynamically create buttons for each level
        for (int i = 1; i <= totalLevels; i++) {
            Button levelButton = new Button(this);

            // Remove button text
            levelButton.setText("");

            // Allow button to wrap its content (image size)
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.setMargins(16, 16, 16, 16);  // Add margins around each button
            levelButton.setLayoutParams(params);

            // Set the background image for the button based on the level number
            String backgroundName = "level_bg_" + i;  // Name of the background drawable for each level
            int backgroundResource = getResources().getIdentifier(backgroundName, "drawable", getPackageName());

            if (backgroundResource != 0) {  // Ensure the drawable resource exists
                Drawable background = ContextCompat.getDrawable(this, backgroundResource);
                levelButton.setBackground(background);
            } else {
                // If no specific background found, set a default background (optional)
                levelButton.setBackgroundResource(R.drawable.default_level_bg);
            }

            // Set an onClickListener for each button to start the selected level
            final int level = i;  // Capture the level number
            levelButton.setOnClickListener(v -> {
                Toast.makeText(LevelSelectActivity.this, "Starting Level " + level, Toast.LENGTH_SHORT).show();
                startGameAtLevel(level);
            });

            // Add the button to the GridLayout
            levelGrid.addView(levelButton);
        }
    }

    // Method to start the game at the selected level
    private void startGameAtLevel(int level) {
        Intent intent = new Intent(LevelSelectActivity.this, MainActivity.class);
        intent.putExtra("LEVEL", level);  // Pass the selected level to MainActivity
        startActivity(intent);
    }
}
