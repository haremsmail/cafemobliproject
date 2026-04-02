package com.example.projectyear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projectyear.database.CafeDatabase;
import com.example.projectyear.database.User;

public class LoginActivitySimple extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;
    private CafeDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            database = CafeDatabase.getInstance(this);
            initViews();
            setupListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);
    }

    private void setupListeners() {
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> login());
        }
        if (btnSignup != null) {
            btnSignup.setOnClickListener(v -> signup());
        }
    }

    private void login() {
        try {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    User user = database.userDao().getUserByEmail(email);
                    String hash = hashPassword(password);

                    runOnUiThread(() -> {
                        if (user != null && user.passwordHash.equals(hash)) {
                            saveSession(user.id);
                            startActivity(new Intent(LoginActivitySimple.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivitySimple.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signup() {
        try {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    User existing = database.userDao().getUserByEmail(email);

                    runOnUiThread(() -> {
                        if (existing != null) {
                            Toast.makeText(LoginActivitySimple.this, "Email already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            String hash = hashPassword(password);
                            User newUser = new User(email, hash);

                            new Thread(() -> {
                                try {
                                    long id = database.userDao().insertUser(newUser);
                                    runOnUiThread(() -> {
                                        saveSession((int) id);
                                        Toast.makeText(LoginActivitySimple.this, "Account created!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivitySimple.this, MainActivity.class));
                                        finish();
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSession(int userId) {
        SharedPreferences prefs = getSharedPreferences("cafe_app", MODE_PRIVATE);
        prefs.edit().putInt("user_id", userId).apply();
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}

