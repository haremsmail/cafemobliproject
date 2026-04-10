package com.example.projectyear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.MainActivity;
import com.example.projectyear.R;
import com.example.projectyear.adapters.FeaturedItemAdapter;
import com.example.projectyear.database.DatabaseHelper;
import com.example.projectyear.database.MenuItem;
import com.example.projectyear.viewmodels.AuthViewModel;
import com.example.projectyear.viewmodels.CartViewModel;
import com.example.projectyear.viewmodels.MenuViewModel;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private MenuViewModel menuViewModel;
    private CartViewModel cartViewModel;
    private AuthViewModel authViewModel;
    private FeaturedItemAdapter featuredAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuViewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Initialize database on background thread
        new Thread(() -> {
            DatabaseHelper.initializeDatabase(requireContext());
            view.post(() -> menuViewModel.loadAll());
        }).start();

        setupGreeting(view);
        setupCategories(view);
        setupFeaturedList(view);
        observeMenu();
    }

    private void setupGreeting(View view) {
        TextView tvGreeting = view.findViewById(R.id.tv_greeting);
        TextView tvName = view.findViewById(R.id.tv_user_name);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12) greeting = "Good morning";
        else if (hour < 17) greeting = "Good afternoon";
        else greeting = "Good evening";

        tvGreeting.setText(greeting);

        String email = authViewModel.getCurrentEmail();
        String name = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        tvName.setText(name.isEmpty() ? "Welcome!" : capitalize(name) + "!");
    }

    private void setupCategories(View view) {
        LinearLayout catCoffee = view.findViewById(R.id.cat_coffee);
        LinearLayout catTea = view.findViewById(R.id.cat_tea);
        LinearLayout catDesserts = view.findViewById(R.id.cat_desserts);

        catCoffee.setOnClickListener(v -> navigateToMenu("Coffee"));
        catTea.setOnClickListener(v -> navigateToMenu("Tea"));
        catDesserts.setOnClickListener(v -> navigateToMenu("Desserts"));
    }

    private void setupFeaturedList(View view) {
        RecyclerView rvFeatured = view.findViewById(R.id.rv_featured);
        rvFeatured.setLayoutManager(
            new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        featuredAdapter = new FeaturedItemAdapter(requireContext(), null, item -> {
            // Add to cart when tapped
            cartViewModel.addItem(item.id, item.name, item.price);
            navigateToMenu("All");
        });
        rvFeatured.setAdapter(featuredAdapter);
        menuViewModel.loadAll();
    }

    private void observeMenu() {
        menuViewModel.getMenuItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null && !items.isEmpty()) {
                // Show max 6 for featured (Coffee items first)
                List<MenuItem> featured = items.size() > 6 ? items.subList(0, 6) : items;
                featuredAdapter.updateItems(featured);
            }
        });
    }

    private void navigateToMenu(String category) {
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).navigateTo(R.id.nav_menu);
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
