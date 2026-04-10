package com.example.projectyear;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectyear.viewmodels.AuthViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnSignup;
    private ProgressBar progressLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);
        progressLogin = findViewById(R.id.progress_login);

        observeViewModel();

        btnLogin.setOnClickListener(v -> {
            clearErrors();
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String pass = etPassword.getText() != null ? etPassword.getText().toString() : "";
            authViewModel.login(email, pass);
        });

        btnSignup.setOnClickListener(v -> {
            clearErrors();
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String pass = etPassword.getText() != null ? etPassword.getText().toString() : "";
            authViewModel.signup(email, pass);
        });
    }

    private void observeViewModel() {
        authViewModel.getAuthState().observe(this, state -> {
            switch (state) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    navigateToMain();
                    break;
                case ERROR:
                    setLoading(false);
                    break;
                case IDLE:
                default:
                    setLoading(false);
                    break;
            }
        });

        authViewModel.getAuthError().observe(this, error -> {
            switch (error) {
                case EMPTY_EMAIL:
                    tilEmail.setError(getString(R.string.error_empty_email));
                    break;
                case INVALID_EMAIL:
                    tilEmail.setError(getString(R.string.error_invalid_email));
                    break;
                case EMPTY_PASSWORD:
                    tilPassword.setError(getString(R.string.error_empty_password));
                    break;
                case SHORT_PASSWORD:
                    tilPassword.setError(getString(R.string.error_password_short));
                    break;
                case INVALID_CREDENTIALS:
                    Snackbar.make(btnLogin, R.string.error_invalid_credentials, Snackbar.LENGTH_LONG).show();
                    break;
                case EMAIL_TAKEN:
                    tilEmail.setError(getString(R.string.error_email_taken));
                    break;
                case NONE:
                default:
                    break;
            }
        });
    }

    private void clearErrors() {
        tilEmail.setError(null);
        tilPassword.setError(null);
        authViewModel.resetState();
    }

    private void setLoading(boolean loading) {
        progressLogin.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        btnSignup.setEnabled(!loading);
    }

    private void navigateToMain() {
        startActivity(new Intent(this, TableSelectionActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
