package com.amaan.tictactoe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tictactoe.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // USERS TABLE
        db.execSQL(
            "CREATE TABLE settings (" +
                "id INTEGER PRIMARY KEY, " +
                "player1_name TEXT," +
                "player2_name TEXT," +
                "bot_difficulty INTEGER" +
            ")"
        );

        db.execSQL(
            "INSERT INTO settings (id, player1_name, player2_name, bot_difficulty) " +
                "VALUES (1, 'Player 1', 'Player 2', 9)"
        );

        // HISTORY TABLE
        db.execSQL(
            "CREATE TABLE history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +

                "games TEXT, " +

                "player1_name TEXT, " +
                "player1_score INTEGER, " +

                "player2_name TEXT, " +
                "player2_score INTEGER, " +

                "draws INTEGER, " +

                "timestamp INTEGER" +        // store as epoch time
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS history");
        onCreate(db);
    }
}