package com.example.projectyear.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.LoginActivity;
import com.example.projectyear.R;
import com.example.projectyear.adapters.OrderHistoryAdapter;
import com.example.projectyear.viewmodels.AuthViewModel;
import com.example.projectyear.viewmodels.OrderViewModel;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private AuthViewModel authViewModel;
    private OrderViewModel orderViewModel;
    private OrderHistoryAdapter orderAdapter;
    private TextView tvEmail, tvAvatar, tvOrdersCount;
    private LinearLayout llNoOrders;
    private ProgressBar progressProfile;
    private RecyclerView rvOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        tvEmail = view.findViewById(R.id.tv_profile_email);
        tvAvatar = view.findViewById(R.id.tv_avatar);
        tvOrdersCount = view.findViewById(R.id.tv_orders_count);
        llNoOrders = view.findViewById(R.id.ll_no_orders);
        progressProfile = view.findViewById(R.id.progress_profile);
        rvOrders = view.findViewById(R.id.rv_orders);
        MaterialButton btnLogout = view.findViewById(R.id.btn_logout);

        setupProfile();
        setupOrderHistory();
        observeOrders();

        btnLogout.setOnClickListener(v -> {
            authViewModel.logout();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void setupProfile() {
        String email = authViewModel.getCurrentEmail();
        tvEmail.setText(email);
        // Initials avatar
        String initial = email != null && !email.isEmpty()
            ? String.valueOf(email.charAt(0)).toUpperCase() : "?";
        tvAvatar.setText(initial);

        orderViewModel.loadOrderHistory(authViewModel.getCurrentUserId());
    }

    private void setupOrderHistory() {
        orderAdapter = new OrderHistoryAdapter(requireContext(), null);
        rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOrders.setAdapter(orderAdapter);
    }

    private void observeOrders() {
        orderViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            progressProfile.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        orderViewModel.getOrderHistory().observe(getViewLifecycleOwner(), orders -> {
            if (orders == null || orders.isEmpty()) {
                rvOrders.setVisibility(View.GONE);
                llNoOrders.setVisibility(View.VISIBLE);
                tvOrdersCount.setText("0 orders");
            } else {
                rvOrders.setVisibility(View.VISIBLE);
                llNoOrders.setVisibility(View.GONE);
                tvOrdersCount.setText(orders.size() + " orders");
                orderAdapter.updateItems(orders);
            }
        });
    }
}
