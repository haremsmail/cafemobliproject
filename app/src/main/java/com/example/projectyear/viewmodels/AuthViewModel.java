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
                // Ensure user exists in Room DB before finishing login
                String cleanEmail = email.trim();
                User user = db.userDao().getUserByEmail(cleanEmail);
                if (user == null) {
                    // Create in Room if missing (legacy migration)
                    long id = db.userDao().insertUser(new User(cleanEmail, password));
                    appPrefs.edit().putInt("user_id", (int) id).apply();
                } else {
                    appPrefs.edit().putInt("user_id", user.id).apply();
                }
                
                saveSession(cleanEmail);
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
            
            String cleanEmail = email.trim();
            usersPrefs.edit().putString(cleanEmail, password).apply();
            
            // Insert into Room and get the real ID
            try {
                long userId = db.userDao().insertUser(new User(cleanEmail, password));
                appPrefs.edit().putInt("user_id", (int) userId).commit(); // commit for immediate sync
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            saveSession(cleanEmail);
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
        return appPrefs.getInt("user_id", -1);
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
