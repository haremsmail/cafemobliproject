package com.example.projectyear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.database.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying order history
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrderHistoryAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        try {
            if (orders == null || position >= orders.size()) {
                return;
            }

            Order order = orders.get(position);
            if (order == null) {
                return;
            }

            if (holder.tvOrderId != null) {
                holder.tvOrderId.setText("Order #" + order.id);
            }
            if (holder.tvOrderDate != null) {
                holder.tvOrderDate.setText(formatDate(order.orderDate));
            }
            if (holder.tvOrderTotal != null) {
                holder.tvOrderTotal.setText(String.format("Rs. %.2f", order.totalPrice));
            }
            if (holder.tvOrderStatus != null) {
                holder.tvOrderStatus.setText("Status: " + (order.status != null ? order.status : ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    /**
     * Update items in the adapter and notify changes
     */
    public void updateItems(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    /**
     * Format timestamp to readable date
     */
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        TextView tvOrderDate;
        TextView tvOrderTotal;
        TextView tvOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                tvOrderId = itemView.findViewById(R.id.tv_order_id);
                tvOrderDate = itemView.findViewById(R.id.tv_order_date);
                tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
                tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

