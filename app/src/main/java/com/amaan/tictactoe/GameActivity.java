package com.amaan.tictactoe;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

    ColorStateList defaultTintList;

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

        int defaultBackground = getThemeColor(android.R.attr.colorBackground);
        int defaultText = getThemeColor(com.google.android.material.R.attr.colorOnBackground);
        defaultTintList = ColorStateList.valueOf(defaultBackground);

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

        ImageButton previous = findViewById(R.id.previous);
        ImageButton next = findViewById(R.id.next);
        ImageButton reset = findViewById(R.id.reset);

        previous.setOnClickListener(v -> {
            game.undo(1);
            updateUI();
        });

        next.setOnClickListener(v -> {
            game.redo(1);
            updateUI();
        });

        reset.setOnClickListener(v -> {
            game.reset();
            updateUI();
        });
    }

    private void updateUI() {
        gameModeOutput.setText(gameMode);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setText("");
                cells[i][j].setBackgroundTintList(defaultTintList);

                if (game.board.grid[i][j] == Board.X) {
                    cells[i][j].setText("X");
                    cells[i][j].setTextColor(getColor(R.color.red));

                    if (game.board.winnerCell[i][j]) {
                        cells[i][j].setTextColor(getColor(R.color.white));
                        cells[i][j].setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
                    }
                }
                else if (game.board.grid[i][j] == Board.O) {
                    cells[i][j].setText("O");
                    cells[i][j].setTextColor(getColor(R.color.blue));

                    if (game.board.winnerCell[i][j]) {
                        cells[i][j].setTextColor(getColor(R.color.white));
                        cells[i][j].setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.blue)));
                    }
                }

            }
        }
    }

    private void handleClick(int i, int j) {
        boolean success = game.move(i, j);

        if (gameMode.equals("Play With Bot") && success) game.botMove();

        updateUI();
    }

    private int getThemeColor(int attr) {
        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(attr, value, true);
        return value.data;
    }
}