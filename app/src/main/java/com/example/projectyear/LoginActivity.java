package com.example.projectyear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.et_email);
        EditText passwordInput = findViewById(R.id.et_password);
        Button loginBtn = findViewById(R.id.btn_login);
        Button signupBtn = findViewById(R.id.btn_signup);

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
            String savedPassword = prefs.getString(email, null);

            if (savedPassword != null && savedPassword.equals(password)) {
                SharedPreferences appPrefs = getSharedPreferences("cafe_app", MODE_PRIVATE);
                appPrefs.edit().putString("logged_in_email", email).apply();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        signupBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
            String existingPassword = prefs.getString(email, null);

            if (existingPassword != null) {
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            prefs.edit().putString(email, password).apply();

            SharedPreferences appPrefs = getSharedPreferences("cafe_app", MODE_PRIVATE);
            appPrefs.edit().putString("logged_in_email", email).apply();

            Toast.makeText(this, "Account created! Welcome!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
}

