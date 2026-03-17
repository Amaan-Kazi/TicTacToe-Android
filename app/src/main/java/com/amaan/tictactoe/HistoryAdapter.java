package com.amaan.tictactoe;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public HistoryAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView player1;
        TextView player2;
        TextView xScore;
        TextView oScore;
        TextView drawScore;
        TextView timestamp;
        LinearLayout roundContainer;

        public ViewHolder(View view) {
            super(view);

            player1 = view.findViewById(R.id.textView7);
            player2 = view.findViewById(R.id.textView9);

            xScore = view.findViewById(R.id.xScore);
            oScore = view.findViewById(R.id.oScore);
            drawScore = view.findViewById(R.id.drawScore);

            timestamp = view.findViewById(R.id.timestamp);

            roundContainer = view.findViewById(R.id.roundContainer);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.history_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!cursor.moveToPosition(position)) return;

        String player1Name = cursor.getString(cursor.getColumnIndexOrThrow("player1_name"));
        String player2Name = cursor.getString(cursor.getColumnIndexOrThrow("player2_name"));

        int p1Score = cursor.getInt(cursor.getColumnIndexOrThrow("player1_score"));
        int p2Score = cursor.getInt(cursor.getColumnIndexOrThrow("player2_score"));
        int draws = cursor.getInt(cursor.getColumnIndexOrThrow("draws"));

        long time = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"));
        String games = cursor.getString(cursor.getColumnIndexOrThrow("games"));

        holder.player1.setText(player1Name);
        holder.player2.setText(player2Name);

        holder.xScore.setText(String.valueOf(p1Score));
        holder.oScore.setText(String.valueOf(p2Score));
        holder.drawScore.setText(String.valueOf(draws));

        String formattedDate = DateFormat.format("yyyy-MM-dd HH:mm", new Date(time)).toString();
        holder.timestamp.setText("Timestamp: " + formattedDate);

        holder.roundContainer.removeAllViews();

//        if (games != null && !games.isEmpty()) {
//
//            String[] rounds = games.split(";");
//
//            for (int i = 0; i < rounds.length; i++) {
//
//                TextView roundView = new TextView(context);
//
//                roundView.setText("Round " + (i + 1) + ": " + rounds[i]);
//
//                roundView.setTextSize(14);
//
//                holder.roundContainer.addView(roundView);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void updateCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        notifyDataSetChanged();
    }
}