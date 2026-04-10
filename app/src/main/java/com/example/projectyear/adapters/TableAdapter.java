package com.example.projectyear.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;

/**
 * Adapter for displaying selectable table cards in a grid.
 * Each table shows a number, icon, and seat count.
 */
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    public interface OnTableSelectedListener {
        void onTableSelected(int tableNumber);
    }

    private final Context context;
    private final int tableCount;
    private int selectedPosition = -1;
    private final OnTableSelectedListener listener;

    // Seat configurations for tables (alternating 2/4 seats)
    private static final int[] SEAT_COUNTS = {2, 4, 2, 4, 6, 2, 4, 4, 2, 6, 4, 2};

    public TableAdapter(Context context, int tableCount, OnTableSelectedListener listener) {
        this.context = context;
        this.tableCount = tableCount;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        int tableNum = position + 1;
        boolean isSelected = position == selectedPosition;
        int seats = SEAT_COUNTS[position % SEAT_COUNTS.length];

        holder.tvTableNumber.setText("Table " + tableNum);
        holder.tvTableSeats.setText(seats + " seats");

        if (isSelected) {
            holder.llCard.setBackgroundResource(R.drawable.bg_table_card_selected);
            holder.tvTableNumber.setTextColor(0xFFFFFBF7); // cream white
            holder.tvTableSeats.setTextColor(0xFFD8C7C5);  // light outline
            holder.tvTableIcon.setText("✅");
        } else {
            holder.llCard.setBackgroundResource(R.drawable.bg_table_card);
            holder.tvTableNumber.setTextColor(context.getResources().getColor(R.color.md_on_surface, null));
            holder.tvTableSeats.setTextColor(context.getResources().getColor(R.color.md_on_surface_variant, null));
            holder.tvTableIcon.setText("🍽️");
        }

        holder.llCard.setOnClickListener(v -> {
            int prevSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Animate the selection
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.llCard, "scaleX", 0.92f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.llCard, "scaleY", 0.92f, 1f);
            AnimatorSet bounce = new AnimatorSet();
            bounce.playTogether(scaleX, scaleY);
            bounce.setDuration(200);
            bounce.start();

            if (prevSelected != -1) notifyItemChanged(prevSelected);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onTableSelected(tableNum);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tableCount;
    }

    public int getSelectedTable() {
        return selectedPosition >= 0 ? selectedPosition + 1 : -1;
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llCard;
        TextView tvTableIcon;
        TextView tvTableNumber;
        TextView tvTableSeats;

        TableViewHolder(@NonNull View itemView) {
            super(itemView);
            llCard = itemView.findViewById(R.id.ll_table_card);
            tvTableIcon = itemView.findViewById(R.id.tv_table_icon);
            tvTableNumber = itemView.findViewById(R.id.tv_table_number);
            tvTableSeats = itemView.findViewById(R.id.tv_table_seats);
        }
    }
}
