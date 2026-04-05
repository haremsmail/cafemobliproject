package com.example.projectyear;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectyear.viewmodels.AuthViewModel;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View content = findViewById(R.id.ll_splash_content);

        // Animate: fade + scale in
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(700);

        ScaleAnimation scaleIn = new ScaleAnimation(
            0.7f, 1f, 0.7f, 1f,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleIn.setDuration(700);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(fadeIn);
        animSet.addAnimation(scaleIn);
        animSet.setFillAfter(true);

        content.animate().alpha(1f).setDuration(700).setStartDelay(100).start();
        content.setAlpha(0f);

        content.postDelayed(() -> {
            AuthViewModel authViewModel = new AuthViewModel(getApplication());
            Intent next = authViewModel.isLoggedIn()
                ? new Intent(this, MainActivity.class)
                : new Intent(this, LoginActivity.class);
            startActivity(next);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 1800);
    }
}
