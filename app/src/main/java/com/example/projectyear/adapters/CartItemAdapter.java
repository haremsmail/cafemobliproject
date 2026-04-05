package com.example.projectyear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.models.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    public interface OnCartUpdateListener {
        void onCartUpdated();
        void onItemRemoved(String itemName);
    }

    private final Context context;
    private List<Cart.CartItem> items;
    private final OnCartUpdateListener listener;

    public CartItemAdapter(Context context, List<Cart.CartItem> items, OnCartUpdateListener listener) {
        this.context = context;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        final Cart.CartItem item = items.get(position);

        holder.tvName.setText(item.itemName);
        holder.tvUnitPrice.setText(String.format("IQD %.0f each", item.price));
        holder.tvQuantity.setText(String.valueOf(item.quantity));
        holder.tvItemTotal.setText(String.format("IQD %.0f", item.getTotalPrice()));

        holder.btnDecrease.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            final Cart.CartItem current = items.get(pos);
            if (current.quantity > 1) {
                Cart.updateQuantity(current.menuItemId, current.quantity - 1);
            } else {
                Cart.removeItem(current.menuItemId);
            }
            if (listener != null) listener.onCartUpdated();
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            final Cart.CartItem current = items.get(pos);
            Cart.updateQuantity(current.menuItemId, current.quantity + 1);
            if (listener != null) listener.onCartUpdated();
        });

        holder.btnRemove.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            final Cart.CartItem current = items.get(pos);
            String name = current.itemName;
            Cart.removeItem(current.menuItemId);
            if (listener != null) {
                listener.onItemRemoved(name);
                listener.onCartUpdated();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<Cart.CartItem> newItems) {
        final List<Cart.CartItem> safeNew = (newItems == null) ? new ArrayList<>() : newItems;
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            final List<Cart.CartItem> old = items;
            final List<Cart.CartItem> next = safeNew;
            @Override public int getOldListSize() { return old.size(); }
            @Override public int getNewListSize() { return next.size(); }
            @Override public boolean areItemsTheSame(int o, int n) {
                return old.get(o).menuItemId == next.get(n).menuItemId;
            }
            @Override public boolean areContentsTheSame(int o, int n) {
                return old.get(o).quantity == next.get(n).quantity;
            }
        });
        items = new ArrayList<>(safeNew);
        result.dispatchUpdatesTo(this);
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUnitPrice, tvQuantity, tvItemTotal;
        ImageButton btnDecrease, btnIncrease, btnRemove;

        CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_name_cart);
            tvUnitPrice = itemView.findViewById(R.id.tv_unit_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvItemTotal = itemView.findViewById(R.id.tv_item_total);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnRemove = itemView.findViewById(R.id.btn_remove_item);
        }
    }
}
