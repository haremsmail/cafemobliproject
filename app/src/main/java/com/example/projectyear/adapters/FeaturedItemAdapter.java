package com.example.projectyear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.database.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class FeaturedItemAdapter extends RecyclerView.Adapter<FeaturedItemAdapter.FeaturedViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    private final Context context;
    private List<MenuItem> items;
    private final OnItemClickListener listener;

    public FeaturedItemAdapter(Context context, List<MenuItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_featured, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        MenuItem item = items.get(position);
        holder.tvEmoji.setText(getEmoji(item.category));
        holder.tvName.setText(item.name);
        holder.tvPrice.setText(String.format("IQD %.0f", item.price));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void updateItems(List<MenuItem> newItems) {
        this.items = newItems != null ? new ArrayList<>(newItems) : new ArrayList<>();
        notifyDataSetChanged();
    }

    private String getEmoji(String category) {
        if (category == null) return "☕";
        switch (category) {
            case "Coffee": return "☕";
            case "Tea": return "🍵";
            case "Desserts": return "🍰";
            default: return "☕";
        }
    }

    static class FeaturedViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName, tvPrice;

        FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji = itemView.findViewById(R.id.tv_featured_emoji);
            tvName = itemView.findViewById(R.id.tv_featured_name);
            tvPrice = itemView.findViewById(R.id.tv_featured_price);
        }
    }
}
