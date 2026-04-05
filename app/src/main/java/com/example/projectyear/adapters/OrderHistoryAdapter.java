package com.example.projectyear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.database.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private final Context context;
    private List<Order> orders;

    public OrderHistoryAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvOrderId.setText(String.format("Order #%d", order.id));
        holder.tvOrderDate.setText(formatDate(order.orderDate));
        holder.tvOrderTotal.setText(String.format("IQD %.0f", order.totalPrice));

        String status = order.status != null ? order.status : "pending";
        holder.tvOrderStatus.setText(capitalize(status));

        // Status chip color
        int bgColor, textColor;
        switch (status.toLowerCase()) {
            case "confirmed":
            case "ready":
                bgColor = ContextCompat.getColor(context, R.color.md_tertiary_container);
                textColor = ContextCompat.getColor(context, R.color.status_confirmed);
                break;
            case "pending":
                bgColor = ContextCompat.getColor(context, R.color.md_secondary_container);
                textColor = ContextCompat.getColor(context, R.color.status_pending);
                break;
            default:
                bgColor = ContextCompat.getColor(context, R.color.md_surface_variant);
                textColor = ContextCompat.getColor(context, R.color.md_on_surface_variant);
        }
        holder.tvOrderStatus.setBackgroundColor(bgColor);
        holder.tvOrderStatus.setTextColor(textColor);
    }

    @Override
    public int getItemCount() { return orders.size(); }

    public void updateItems(List<Order> newOrders) {
        this.orders = newOrders != null ? new ArrayList<>(newOrders) : new ArrayList<>();
        notifyDataSetChanged();
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderTotal, tvOrderStatus;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
        }
    }
}
