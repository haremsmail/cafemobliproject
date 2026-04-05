package com.example.projectyear.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectyear.database.CafeDatabase;
import com.example.projectyear.database.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthViewModel extends AndroidViewModel {

    public enum AuthState { IDLE, LOADING, SUCCESS, ERROR }
    public enum AuthError { NONE, EMPTY_EMAIL, INVALID_EMAIL, EMPTY_PASSWORD, SHORT_PASSWORD, INVALID_CREDENTIALS, EMAIL_TAKEN }

    private final MutableLiveData<AuthState> authState = new MutableLiveData<>(AuthState.IDLE);
    private final MutableLiveData<AuthError> authError = new MutableLiveData<>(AuthError.NONE);
    private final MutableLiveData<String> loggedInEmail = new MutableLiveData<>("");
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CafeDatabase db;
    private final SharedPreferences usersPrefs;
    private final SharedPreferences appPrefs;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        db = CafeDatabase.getInstance(application);
        usersPrefs = application.getSharedPreferences("users", 0);
        appPrefs = application.getSharedPreferences("cafe_app", 0);
    }

    public LiveData<AuthState> getAuthState() { return authState; }
    public LiveData<AuthError> getAuthError() { return authError; }
    public LiveData<String> getLoggedInEmail() { return loggedInEmail; }

    public void login(String email, String password) {
        if (!validate(email, password, false)) return;
        authState.setValue(AuthState.LOADING);
        executor.execute(() -> {
            String saved = usersPrefs.getString(email.trim(), null);
            if (saved != null && saved.equals(password)) {
                saveSession(email.trim());
                authState.postValue(AuthState.SUCCESS);
            } else {
                authError.postValue(AuthError.INVALID_CREDENTIALS);
                authState.postValue(AuthState.ERROR);
            }
        });
    }

    public void signup(String email, String password) {
        if (!validate(email, password, true)) return;
        authState.setValue(AuthState.LOADING);
        executor.execute(() -> {
            String existing = usersPrefs.getString(email.trim(), null);
            if (existing != null) {
                authError.postValue(AuthError.EMAIL_TAKEN);
                authState.postValue(AuthState.ERROR);
                return;
            }
            usersPrefs.edit().putString(email.trim(), password).apply();
            // Also insert a Room User record
            try {
                db.userDao().insertUser(new User(email.trim(), password));
            } catch (Exception ignored) {}
            saveSession(email.trim());
            authState.postValue(AuthState.SUCCESS);
        });
    }

    public boolean isLoggedIn() {
        return appPrefs.getString("logged_in_email", null) != null;
    }

    public String getCurrentEmail() {
        return appPrefs.getString("logged_in_email", "");
    }

    public int getCurrentUserId() {
        return appPrefs.getInt("user_id", 1);
    }

    public void logout() {
        appPrefs.edit().remove("logged_in_email").remove("user_id").apply();
        authState.setValue(AuthState.IDLE);
    }

    public void resetState() {
        authState.setValue(AuthState.IDLE);
        authError.setValue(AuthError.NONE);
    }

    private void saveSession(String email) {
        appPrefs.edit().putString("logged_in_email", email).apply();
        loggedInEmail.postValue(email);
        // Try to get user id from Room
        executor.execute(() -> {
            try {
                User user = db.userDao().getUserByEmail(email);
                if (user != null) {
                    appPrefs.edit().putInt("user_id", user.id).apply();
                }
            } catch (Exception ignored) {}
        });
    }

    private boolean validate(String email, String password, boolean isSignup) {
        if (email == null || email.trim().isEmpty()) {
            authError.setValue(AuthError.EMPTY_EMAIL);
            authState.setValue(AuthState.ERROR);
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            authError.setValue(AuthError.INVALID_EMAIL);
            authState.setValue(AuthState.ERROR);
            return false;
        }
        if (password == null || password.isEmpty()) {
            authError.setValue(AuthError.EMPTY_PASSWORD);
            authState.setValue(AuthState.ERROR);
            return false;
        }
        if (isSignup && password.length() < 6) {
            authError.setValue(AuthError.SHORT_PASSWORD);
            authState.setValue(AuthState.ERROR);
            return false;
        }
        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
