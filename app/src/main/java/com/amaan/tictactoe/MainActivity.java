package com.amaan.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Game Modes

        Button passAndPlay = findViewById(R.id.pass_and_play);
        Button playWithBot = findViewById(R.id.play_with_bot);
        // Button playViaBluetooth = findViewById(R.id.play_via_bluetooth);

        passAndPlay.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", "Pass And Play");
            startActivity(intent);
        });

        playWithBot.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", "Play With Bot");
            startActivity(intent);
        });

//        playViaBluetooth.setOnClickListener(v -> {
//            Intent intent = new Intent(this, WaitingActivity.class);
//            intent.putExtra("mode", "Play Via Bluetooth");
//            startActivity(intent);
//        });


        // History and Settings

        Button history = findViewById(R.id.history);
        Button settings = findViewById(R.id.settings);

        history.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });


        // Quit Button

        Button quit = findViewById(R.id.quit);

        quit.setOnClickListener(v -> {
            finishAffinity();
        });

    }
}