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
    private View player1DetailsUI;
    private View player2DetailsUI;

    private String gameMode;
    private TextView gameModeOutput;

    int defaultBackground;
    int defaultText;
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
        player1DetailsUI = findViewById(R.id.player1_details);
        player2DetailsUI = findViewById(R.id.player2_details);

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

        defaultBackground = getThemeColor(android.R.attr.colorBackground);
        defaultText = getThemeColor(com.google.android.material.R.attr.colorOnBackground);
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


        TextView piece1 = player1DetailsUI.findViewById(R.id.piece);
        TextView score1 = player1DetailsUI.findViewById(R.id.score);
        TextView player_name1 = player1DetailsUI.findViewById(R.id.player_name);

        player_name1.setBackgroundColor(game.board.xTurn ? getColor(R.color.red) : defaultBackground);
        player_name1.setTextColor(game.board.xTurn ? getColor(R.color.white) : defaultText);
        piece1.setBackgroundColor(getColor(R.color.red));
        score1.setBackgroundColor(getColor(R.color.red));

        piece1.setText("X");
        score1.setText("0");
        player_name1.setText("Player 1");


        TextView piece2 = player2DetailsUI.findViewById(R.id.piece);
        TextView score2 = player2DetailsUI.findViewById(R.id.score);
        TextView player_name2 = player2DetailsUI.findViewById(R.id.player_name);

        player_name2.setBackgroundColor(game.board.xTurn ? defaultBackground : getColor(R.color.blue));
        player_name2.setTextColor(game.board.xTurn ? defaultText : getColor(R.color.white));
        piece2.setBackgroundColor(getColor(R.color.blue));
        score2.setBackgroundColor(getColor(R.color.blue));

        piece2.setText("O");
        score2.setText("0");
        player_name2.setText("Player 2");

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