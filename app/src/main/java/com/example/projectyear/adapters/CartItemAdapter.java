package com.example.projectyear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.models.Cart;

import java.util.List;

/**
 * RecyclerView Adapter for displaying cart items
 */
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private Context context;
    private List<Cart.CartItem> items;
    private OnCartUpdateListener listener;

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    public CartItemAdapter(Context context, List<Cart.CartItem> items) {
        this.context = context;
        this.items = items;
    }

    public CartItemAdapter(Context context, List<Cart.CartItem> items, OnCartUpdateListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_summary, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        try {
            if (items == null || position >= items.size()) {
                return;
            }

            Cart.CartItem item = items.get(position);
            if (item == null) {
                return;
            }

            if (holder.tvItemName != null) {
                holder.tvItemName.setText(item.itemName != null ? item.itemName : "");
            }
            if (holder.tvQuantity != null) {
                holder.tvQuantity.setText("Qty: " + item.quantity);
            }
            if (holder.tvItemTotal != null) {
                holder.tvItemTotal.setText(String.format("Rs. %.2f", item.getTotalPrice()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Update the list of items
     */
    public void updateItems(List<Cart.CartItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvPrice;
        TextView tvQuantity;
        TextView tvItemTotal;
        EditText etQuantity;
        ImageButton btnUpdateQuantity;
        ImageButton btnRemove;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                tvItemName = itemView.findViewById(R.id.tv_item_name_cart);
                tvPrice = itemView.findViewById(R.id.tv_item_name_cart);
                tvQuantity = itemView.findViewById(R.id.tv_quantity);
                tvItemTotal = itemView.findViewById(R.id.tv_item_total);
                etQuantity = null;
                btnUpdateQuantity = null;
                btnRemove = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

