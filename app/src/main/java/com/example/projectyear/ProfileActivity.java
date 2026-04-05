package com.example.projectyear;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences prefs = getSharedPreferences("cafe_app", MODE_PRIVATE);
        String email = prefs.getString("logged_in_email", "Unknown");

        Toast.makeText(this, "👤 Profile: " + email + "\n\nTest Orders:\n- Order #001: IQD 450\n- Order #002: IQD 320", Toast.LENGTH_LONG).show();
    }
}


