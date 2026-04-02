package com.example.projectyear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivityNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("cafe_app", MODE_PRIVATE);
            int userId = prefs.getInt("user_id", -1);

            if (userId > 0) {
                startActivity(new Intent(SplashActivityNew.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivityNew.this, LoginActivity.class));
            }
            finish();
        }, 1500);
    }
}

