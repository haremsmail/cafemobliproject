package com.example.projectyear.fragments;

import android.content.Intent;
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
import com.example.projectyear.OrderConfirmationActivity;
import com.example.projectyear.R;
import com.example.projectyear.adapters.CartItemAdapter;
import com.example.projectyear.viewmodels.AuthViewModel;
import com.example.projectyear.viewmodels.CartViewModel;
import com.example.projectyear.viewmodels.OrderViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private OrderViewModel orderViewModel;
    private AuthViewModel authViewModel;
    private CartItemAdapter cartAdapter;
    private LinearLayout llCartEmpty;
    private RecyclerView rvCart;
    private TextView tvSubtotal, tvTax, tvTotal;
    private MaterialButton btnCheckout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        rvCart = view.findViewById(R.id.rv_cart);
        llCartEmpty = view.findViewById(R.id.ll_cart_empty);
        tvSubtotal = view.findViewById(R.id.tv_subtotal);
        tvTax = view.findViewById(R.id.tv_tax);
        tvTotal = view.findViewById(R.id.tv_total);
        btnCheckout = view.findViewById(R.id.btn_checkout);
        MaterialButton btnGoToMenu = view.findViewById(R.id.btn_go_to_menu);

        setupRecyclerView();
        observeViewModel();
        cartViewModel.refreshCart();

        btnCheckout.setOnClickListener(v -> placeOrder());

        btnGoToMenu.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).navigateTo(R.id.nav_menu);
            }
        });
    }

    private void setupRecyclerView() {
        cartAdapter = new CartItemAdapter(requireContext(), null, new CartItemAdapter.OnCartUpdateListener() {
            @Override public void onCartUpdated() { cartViewModel.refreshCart(); }
            @Override public void onItemRemoved(String itemName) {
                Snackbar.make(requireView(), itemName + " removed", Snackbar.LENGTH_SHORT).show();
            }
        });
        rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCart.setAdapter(cartAdapter);
    }

    private void observeViewModel() {
        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), items -> {
            if (items == null || items.isEmpty()) {
                rvCart.setVisibility(View.GONE);
                llCartEmpty.setVisibility(View.VISIBLE);
            } else {
                rvCart.setVisibility(View.VISIBLE);
                llCartEmpty.setVisibility(View.GONE);
                cartAdapter.updateItems(items);
            }
        });

        cartViewModel.getSubtotal().observe(getViewLifecycleOwner(), sub ->
            tvSubtotal.setText(String.format("IQD %.0f", sub)));
        cartViewModel.getTax().observe(getViewLifecycleOwner(), tax ->
            tvTax.setText(String.format("IQD %.0f", tax)));
        cartViewModel.getTotal().observe(getViewLifecycleOwner(), total ->
            tvTotal.setText(String.format("IQD %.0f", total)));

        orderViewModel.getLastOrderId().observe(getViewLifecycleOwner(), orderId -> {
            if (orderId != null && orderId > 0) {
                double total = cartViewModel.getTotal().getValue() != null
                    ? cartViewModel.getTotal().getValue() : 0;
                cartViewModel.clearCart();
                Intent intent = new Intent(requireActivity(), OrderConfirmationActivity.class);
                intent.putExtra("order_id", orderId);
                intent.putExtra("order_total", total);
                startActivity(intent);
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void placeOrder() {
        if (cartViewModel.isCartEmpty()) {
            Snackbar.make(requireView(), R.string.msg_cart_empty_error, Snackbar.LENGTH_SHORT).show();
            return;
        }
        int userId = authViewModel.getCurrentUserId();
        orderViewModel.placeOrder(userId);
    }

    @Override
    public void onResume() {
        super.onResume();
        cartViewModel.refreshCart();
    }
}
