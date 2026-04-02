package com.example.projectyear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("cafe_app", MODE_PRIVATE);
        String email = prefs.getString("logged_in_email", null);

        if (email == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Toast.makeText(this, "Welcome " + email, Toast.LENGTH_SHORT).show();

        Button btnBrowseMenu = findViewById(R.id.btn_browse_menu);
        Button btnViewOrders = findViewById(R.id.btn_view_orders);
        Button btnProfile = findViewById(R.id.btn_profile);
        Button btnLogout = findViewById(R.id.btn_logout);

        btnBrowseMenu.setOnClickListener(v ->
            startActivity(new Intent(MainActivity.this, MenuActivity.class)));

        btnViewOrders.setOnClickListener(v ->
            startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        btnProfile.setOnClickListener(v ->
            startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        btnLogout.setOnClickListener(v -> {
            prefs.edit().remove("logged_in_email").apply();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}

