package com.example.projectyear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.database.MenuItem;
import com.example.projectyear.models.Cart;

import java.util.List;

/**
 * RecyclerView Adapter for displaying menu items
 */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private Context context;
    private List<MenuItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAddToCart(MenuItem item);
    }

    public MenuItemAdapter(Context context, List<MenuItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
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
        try {
            if (items == null || position >= items.size()) {
                return;
            }

            MenuItem item = items.get(position);
            if (item == null) {
                return;
            }

            if (holder.tvItemName != null) {
                holder.tvItemName.setText(item.name != null ? item.name : "");
            }
            if (holder.tvDescription != null) {
                holder.tvDescription.setText(item.description != null ? item.description : "");
            }
            if (holder.tvPrice != null) {
                holder.tvPrice.setText(String.format("Rs. %.2f", item.price));
            }
            if (holder.tvCategory != null) {
                holder.tvCategory.setText(item.category != null ? item.category : "");
            }

            if (holder.ivItemImage != null) {
                if (item.imageResource != 0) {
                    holder.ivItemImage.setImageResource(item.imageResource);
                } else {
                    holder.ivItemImage.setImageResource(R.drawable.ic_launcher_background);
                }
            }

            if (holder.btnAddToCart != null) {
                holder.btnAddToCart.setOnClickListener(v -> {
                    try {
                        Cart.addItem(item.id, item.name, item.price);
                        Toast.makeText(context, item.name + " added to cart!", Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onAddToCart(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
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
    public void updateItems(List<MenuItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItemImage;
        TextView tvItemName;
        TextView tvDescription;
        TextView tvPrice;
        TextView tvCategory;
        Button btnAddToCart;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                ivItemImage = itemView.findViewById(R.id.iv_item_image);
                tvItemName = itemView.findViewById(R.id.tv_item_name);
                tvDescription = itemView.findViewById(R.id.tv_description);
                tvPrice = itemView.findViewById(R.id.tv_price);
                tvCategory = itemView.findViewById(R.id.tv_category);
                btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

