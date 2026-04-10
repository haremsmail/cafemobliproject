package com.example.projectyear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.adapters.MenuItemAdapter;
import com.example.projectyear.viewmodels.CartViewModel;
import com.example.projectyear.viewmodels.MenuViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

public class MenuFragment extends Fragment {

    private MenuViewModel menuViewModel;
    private CartViewModel cartViewModel;
    private MenuItemAdapter adapter;
    private RecyclerView rvMenu;
    private ProgressBar progressMenu;
    private LinearLayout llEmptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuViewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        rvMenu = view.findViewById(R.id.rv_menu);
        progressMenu = view.findViewById(R.id.progress_menu);
        llEmptyState = view.findViewById(R.id.ll_empty_state);

        setupRecyclerView();
        setupChipFilter(view);
        observeViewModel();

        menuViewModel.loadAll();
    }

    private void setupRecyclerView() {
        adapter = new MenuItemAdapter(requireContext(), null, item -> {
            cartViewModel.addItem(item.id, item.name, item.price);
            Snackbar.make(requireView(),
                item.name + " added to cart",
                Snackbar.LENGTH_SHORT).show();
        });
        rvMenu.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvMenu.setAdapter(adapter);
    }

    private void setupChipFilter(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.chip_group_category);

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int checkedId = checkedIds.get(0);
            String category;
            if (checkedId == R.id.chip_coffee) category = "Coffee";
            else if (checkedId == R.id.chip_tea) category = "Tea";
            else if (checkedId == R.id.chip_desserts) category = "Desserts";
            else category = "All";
            menuViewModel.loadMenu(category);
        });
    }

    private void observeViewModel() {
        menuViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            progressMenu.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        menuViewModel.getMenuItems().observe(getViewLifecycleOwner(), items -> {
            if (items == null || items.isEmpty()) {
                rvMenu.setVisibility(View.GONE);
                llEmptyState.setVisibility(View.VISIBLE);
            } else {
                rvMenu.setVisibility(View.VISIBLE);
                llEmptyState.setVisibility(View.GONE);
                adapter.updateItems(items);
            }
        });
    }
}
