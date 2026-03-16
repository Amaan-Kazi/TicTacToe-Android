package com.amaan.tictactoe;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText player1_name = findViewById(R.id.player1_name);
        EditText player2_name = findViewById(R.id.player2_name);
        EditText bot_difficulty = findViewById(R.id.bot_difficulty);

        Button submit = findViewById(R.id.submit);



        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper .getWritableDatabase();

        android.database.Cursor cursor =
                db.rawQuery("SELECT player1_name, player2_name, bot_difficulty FROM settings LIMIT 1", null);

        if (cursor.moveToFirst()) {
            player1_name.setText(cursor.getString(0));
            player2_name.setText(cursor.getString(1));
            bot_difficulty.setText(cursor.getString(2));
        }

        cursor.close();


        submit.setOnClickListener(v -> {
            String name1 = player1_name.getText().toString();
            String name2 = player2_name.getText().toString();
            int difficulty = Integer.parseInt(bot_difficulty.getText().toString());

            android.content.ContentValues values = new android.content.ContentValues();
            values.put("player1_name", name1);
            values.put("player2_name", name2);
            values.put("bot_difficulty", difficulty);

            db.update("settings", values, "id = ?", new String[]{"1"});

            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
        });

    }
}
