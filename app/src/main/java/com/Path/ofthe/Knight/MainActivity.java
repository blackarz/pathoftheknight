package com.Path.ofthe.Knight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSound;
    private boolean isSoundEffectsOn;

    private MediaPlayer backgroundMusic;
    private boolean isMusicOn;

    private SharedPreferences preferences;

    private ImageView BackBtn,SettingsBtn;
    private ArrayList<Integer> originalOrder;
    private ArrayList<Integer> currentOrder;
    private TextView scoreText, levelText;
    private GridLayout elementGrid;
    private int score = 0;
    private int level = 1;
    private int firstSelectedIndex = -1;  // First element clicked for swapping
    private final int maxLevel = 14;      // Maximum level set to 14

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackBtn = findViewById(R.id.BackBtn);
        SettingsBtn = findViewById(R.id.SettingsBtn);


        scoreText = findViewById(R.id.scoreText);
        levelText = findViewById(R.id.levelText);
        elementGrid = findViewById(R.id.elementGrid);

        // Load settings from SharedPreferences
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        isMusicOn = preferences.getBoolean("musicOn", true);
        isSoundEffectsOn = preferences.getBoolean("soundEffectsOn", true);

        // Initialize background music and sound effects
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music);
        backgroundMusic.setLooping(true);  // Loop the music
        if (isMusicOn) {
            backgroundMusic.start();  // Start music
        }

        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        clickSound = soundPool.load(this, R.raw.click_sound, 1);

        // Register broadcast receivers
        registerReceiver(musicPrefReceiver, new IntentFilter("UPDATE_MUSIC_PREF"), null, null);
        registerReceiver(soundEffectsPrefReceiver, new IntentFilter("UPDATE_SOUND_EFFECTS_PREF"), null, null);




        // Buttons Settings and Back
        SettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);

            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LevelSelectActivity.class);
                startActivity(intent);
            }
        });



        originalOrder = new ArrayList<>();
        currentOrder = new ArrayList<>();

        // Add elements for the sequence game
        originalOrder.add(R.drawable.element1); // replace with your drawable resource
        originalOrder.add(R.drawable.element2);
        originalOrder.add(R.drawable.element3);
        originalOrder.add(R.drawable.element4);
        originalOrder.add(R.drawable.element5);
        originalOrder.add(R.drawable.element6);

        currentOrder.addAll(originalOrder); // Copy original order to current order

        // Get the level from the intent if passed from LevelSelectActivity
        Intent intent = getIntent();
        if (intent.hasExtra("LEVEL")) {
            level = intent.getIntExtra("LEVEL", 1);  // Start at the selected level
            score = (level - 1) * 500;  // Calculate the score based on the starting level
        }

        // Shuffle elements when moving to a new level
        shuffleElementsForLevel(level);

        displayElements(currentOrder);

        ImageView mixButton = findViewById(R.id.mixButton);
        ImageView submitOrderButton = findViewById(R.id.submitOrderButton);

        // Mix the elements when "Mix" button is pressed
        mixButton.setOnClickListener(v -> {
            Collections.shuffle(currentOrder);
            displayElements(currentOrder);
        });

        // Submit the order and check if it's correct
        submitOrderButton.setOnClickListener(v -> {
            if (originalOrder.equals(currentOrder)) {
                Toast.makeText(MainActivity.this, "Correct Order!", Toast.LENGTH_SHORT).show();
                score += 500;  // Award 500 points for correct answer
                updateUI();
                navigateToNextScreen();
            } else {
                Toast.makeText(MainActivity.this, "Incorrect, try again.", Toast.LENGTH_SHORT).show();
                Intent gameOverIntent = new Intent(MainActivity.this, GameOverActivity.class);
                startActivity(gameOverIntent);
                finish();
            }
        });

        updateUI();
    }

    // Shuffle elements or modify them based on the current level
    private void shuffleElementsForLevel(int level) {
        if (level > 1) {
            // Add more elements or change the game dynamically based on the level
            // For example, add more elements as the level increases

            // Shuffle the current order to increase difficulty
            currentOrder.clear();
            currentOrder.addAll(originalOrder);
            Collections.shuffle(currentOrder);
        }
    }


    // Function to display the elements in the GridLayout
    private void displayElements(ArrayList<Integer> order) {
        elementGrid.removeAllViews();
        for (int i = 0; i < order.size(); i++) {
            int drawableId = order.get(i);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(drawableId);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 200;  // Adjust these values based on your design
            params.height = 200;
            params.setGravity(android.view.Gravity.CENTER);  // Center the ImageView in its grid cell
            imageView.setLayoutParams(params);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            final int index = i;  // Keep track of the index for swapping
            imageView.setOnClickListener(v -> onElementClicked(index));  // Implement swapping logic here
            elementGrid.addView(imageView);
        }
    }


// Handle element click for swapping logic and play sound
    private void onElementClicked(int index) {
        if (isSoundEffectsOn) {
            soundPool.play(clickSound, 1, 1, 0, 0, 1);  // Play click sound if sound effects are enabled
        }

        if (firstSelectedIndex == -1) {
            // First click
            firstSelectedIndex = index;
        } else {
            // Second click - swap the two elements
            Collections.swap(currentOrder, firstSelectedIndex, index);
            displayElements(currentOrder);  // Refresh the grid to show the swapped elements
            firstSelectedIndex = -1;  // Reset for the next swap
        }
    }


    private void updateUI() {
        scoreText.setText("Score: " + score);
        levelText.setText("Level: " + level);
    }


    // BroadcastReceiver to update music preferences dynamically
    private BroadcastReceiver musicPrefReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("UPDATE_MUSIC_PREF")) {
                isMusicOn = preferences.getBoolean("musicOn", true);
                if (isMusicOn) {
                    backgroundMusic.start();
                } else {
                    backgroundMusic.pause();
                }
            }
        }
    };

    // BroadcastReceiver to update sound effects preferences dynamically
    private final BroadcastReceiver soundEffectsPrefReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), "UPDATE_SOUND_EFFECTS_PREF")) {
                isSoundEffectsOn = preferences.getBoolean("soundEffectsOn", true);
                if (isSoundEffectsOn) {
                    soundPool.autoResume();
                } else {
                    soundPool.autoPause();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMusicOn && backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (backgroundMusic != null) {
            backgroundMusic.release();  // Release media player resources
        }

        if (soundPool != null) {
            soundPool.release();  // Release SoundPool resources
        }

        unregisterReceiver(musicPrefReceiver);
        unregisterReceiver(soundEffectsPrefReceiver);
    }

    // Navigate to the next screen (either Level Complete or Game Over)
    private void navigateToNextScreen() {
        if (level < maxLevel) {
            // Go to the Level Complete screen
            Intent levelCompleteIntent = new Intent(MainActivity.this, LevelCompleteActivity.class);
            levelCompleteIntent.putExtra("LEVEL", level);  // Pass the current level
            startActivity(levelCompleteIntent);
            finish();
        } else {
            // Go to the Game Over screen if all levels are complete
            Intent gameOverIntent = new Intent(MainActivity.this, GameOverActivity.class);
            startActivity(gameOverIntent);
            finish();
        }
    }
}
