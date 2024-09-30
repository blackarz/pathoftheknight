package com.Path.ofthe.Knight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView gameOverText = findViewById(R.id.gameOverText);
        ImageView playAgainButton = findViewById(R.id.playAgainButton);
        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        ImageView BackBtn = findViewById(R.id.BackBtn);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        gameOverText.setText("Game Over!");

        // Restart the game from Level 1
        playAgainButton.setOnClickListener(v -> {
            Intent playAgainIntent = new Intent(GameOverActivity.this, MainActivity.class);
            playAgainIntent.putExtra("LEVEL", 1); // Restart from level 1
            startActivity(playAgainIntent);
            finish();
        });

        // Go back to the main menu
        mainMenuButton.setOnClickListener(v -> {
            Intent mainMenuIntent = new Intent(GameOverActivity.this, LevelSelectActivity.class);
            startActivity(mainMenuIntent);
            finish();
        });
    }
}
