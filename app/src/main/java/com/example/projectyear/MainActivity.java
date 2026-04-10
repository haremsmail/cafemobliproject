package com.example.projectyear;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectyear.fragments.CartFragment;
import com.example.projectyear.fragments.HomeFragment;
import com.example.projectyear.fragments.MenuFragment;
import com.example.projectyear.fragments.ProfileFragment;
import com.example.projectyear.viewmodels.AuthViewModel;
import com.example.projectyear.viewmodels.CartViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private CartViewModel cartViewModel;
    private BottomNavigationView bottomNav;
    private int tableNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get table number from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TABLE_NUMBER")) {
            tableNumber = intent.getIntExtra("TABLE_NUMBER", -1);
            setTitle("Table " + tableNumber);
        }

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Guard: redirect to login if not authenticated
        if (!authViewModel.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        bottomNav = findViewById(R.id.bottom_nav);

        setupBottomNav();
        observeCart();

        // Default to Menu if coming from table selection
        if (savedInstanceState == null) {
            if (tableNumber != -1) {
                loadFragment(new MenuFragment());
                bottomNav.setSelectedItemId(R.id.nav_menu);
            } else {
                loadFragment(new HomeFragment());
            }
        }
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment = null;
            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_menu) {
                fragment = new MenuFragment();
            } else if (id == R.id.nav_cart) {
                fragment = new CartFragment();
            } else if (id == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            .commit();
    }

    private void observeCart() {
        cartViewModel.getItemCount().observe(this, count -> {
            BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_cart);
            if (count != null && count > 0) {
                badge.setVisible(true);
                badge.setNumber(count);
            } else {
                badge.setVisible(false);
            }
        });
    }

    /** Called by fragments that need to navigate to a specific tab. */
    public void navigateTo(int navItemId) {
        bottomNav.setSelectedItemId(navItemId);
    }
}
