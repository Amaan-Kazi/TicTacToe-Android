package com.amaan.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {
    private Game game = new Game(3);

    private View boardUI;

    private String gameMode;
    private TextView gameModeOutput;

    private Button[][] cells;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        boardUI = findViewById(R.id.board);

        gameModeOutput = findViewById(R.id.mode);

        Intent intent = getIntent();
        gameMode = intent.getStringExtra("mode");

        cells = new Button[3][3];

        cells[0][0] = boardUI.findViewById(R.id.button00);
        cells[0][1] = boardUI.findViewById(R.id.button01);
        cells[0][2] = boardUI.findViewById(R.id.button02);

        cells[1][0] = boardUI.findViewById(R.id.button10);
        cells[1][1] = boardUI.findViewById(R.id.button11);
        cells[1][2] = boardUI.findViewById(R.id.button12);

        cells[2][0] = boardUI.findViewById(R.id.button20);
        cells[2][1] = boardUI.findViewById(R.id.button21);
        cells[2][2] = boardUI.findViewById(R.id.button22);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int r = i;
                final int c = j;

                cells[i][j].setOnClickListener(v -> {
                    handleClick(r, c);
                });
            }
        }

        updateUI();
    }

    private void updateUI() {
        gameModeOutput.setText(gameMode);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if      (game.board.grid[i][j] == Board.X) cells[i][j].setText("X");
                else if (game.board.grid[i][j] == Board.O) cells[i][j].setText("O");
                else                                       cells[i][j].setText("");
            }
        }
    }

    private void handleClick(int i, int j) {
        game.move(i, j);
        updateUI();
    }
}