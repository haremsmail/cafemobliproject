package com.example.projectyear;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectyear.viewmodels.AuthViewModel;
import com.example.projectyear.viewmodels.CartViewModel;
import com.example.projectyear.viewmodels.OrderViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        long orderId = getIntent().getLongExtra("order_id", -1);
        double total = getIntent().getDoubleExtra("order_total", 0.0);

        TextView tvIcon = findViewById(R.id.tv_confirm_icon);
        TextView tvOrderId = findViewById(R.id.tv_order_id_confirm);
        TextView tvTotal = findViewById(R.id.tv_total_confirm);
        MaterialButton btnHome = findViewById(R.id.btn_back_home);
        MaterialButton btnOrders = findViewById(R.id.btn_view_my_orders);

        // Animate checkmark
        tvIcon.setAlpha(0f);
        tvIcon.setScaleX(0.5f);
        tvIcon.setScaleY(0.5f);
        tvIcon.animate()
            .alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(500).setStartDelay(100).start();

        tvOrderId.setText(orderId > 0 ? "Order #" + orderId : "Order Confirmed");
        tvTotal.setText(String.format("IQD %.0f", total));

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        btnOrders.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("navigate_to", "profile");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent back navigation to cart
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
