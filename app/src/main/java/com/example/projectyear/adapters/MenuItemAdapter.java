package com.example.projectyear.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.database.MenuItem;
import com.example.projectyear.models.Cart;

import java.util.ArrayList;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    public interface OnItemClickListener {
        void onAddToCart(MenuItem item);
    }

    private final Context context;
    private List<MenuItem> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public MenuItemAdapter(Context context, List<MenuItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem item = items.get(position);

        holder.tvEmoji.setText(getEmoji(item.category));
        holder.tvName.setText(item.name);
        holder.tvDescription.setText(item.description);
        holder.tvPrice.setText(String.format("IQD %.0f", item.price));
        holder.tvCategory.setText(item.category);

        boolean inCart = Cart.getAllItems().containsKey(item.id);
        if (inCart) {
            holder.tvAddToCart.setText("Added ✓");
            holder.tvAddToCart.setBackgroundResource(R.drawable.bg_chip_selected);
        } else {
            holder.tvAddToCart.setText("+ Add");
            holder.tvAddToCart.setBackgroundResource(R.drawable.bg_chip_primary);
        }

        holder.tvAddToCart.setOnClickListener(v -> {
            if (listener != null) listener.onAddToCart(item);
            holder.tvAddToCart.setText("Added ✓");
            holder.tvAddToCart.setBackgroundResource(R.drawable.bg_chip_selected);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<MenuItem> newItems) {
        final List<MenuItem> safeNew = (newItems == null) ? new ArrayList<>() : newItems;
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override public int getOldListSize() { return items.size(); }
            @Override public int getNewListSize() { return safeNew.size(); }
            @Override public boolean areItemsTheSame(int oldPos, int newPos) {
                return items.get(oldPos).id == safeNew.get(newPos).id;
            }
            @Override public boolean areContentsTheSame(int oldPos, int newPos) {
                return items.get(oldPos).name.equals(safeNew.get(newPos).name);
            }
        });
        items = new ArrayList<>(safeNew);
        result.dispatchUpdatesTo(this);
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

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName, tvDescription, tvPrice, tvCategory, tvAddToCart;

        MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji = itemView.findViewById(R.id.tv_item_emoji);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvAddToCart = itemView.findViewById(R.id.tv_add_to_cart);
        }
    }
}
