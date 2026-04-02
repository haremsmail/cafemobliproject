package com.example.projectyear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.R;
import com.example.projectyear.adapters.MenuItemAdapter;
import com.example.projectyear.database.CafeDatabase;
import com.example.projectyear.database.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying café menu items in a RecyclerView
 */
public class MenuFragment extends Fragment implements MenuItemAdapter.OnItemClickListener {

    private RecyclerView rvMenu;
    private MenuItemAdapter adapter;
    private List<MenuItem> menuItems = new ArrayList<>();
    private CafeDatabase database;
    private String selectedCategory = "All Items";

    public static MenuFragment newInstance(String category) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedCategory = getArguments().getString("category", "All Items");
        }
        database = CafeDatabase.getInstance(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            rvMenu = view.findViewById(R.id.rv_menu);
            if (rvMenu != null) {
                rvMenu.setLayoutManager(new LinearLayoutManager(requireContext()));
                adapter = new MenuItemAdapter(requireContext(), menuItems, this);
                rvMenu.setAdapter(adapter);
                loadMenuItems();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMenuItems() {
        new Thread(() -> {
            try {
                List<MenuItem> items;
                if (selectedCategory != null && selectedCategory.equals("All Items")) {
                    items = database.menuDao().getAllMenuItems();
                } else {
                    items = database.menuDao().getMenuItemsByCategory(selectedCategory != null ? selectedCategory : "All Items");
                }

                if (items != null) {
                    menuItems.clear();
                    menuItems.addAll(items);
                }

                if (isAdded() && adapter != null) {
                    requireActivity().runOnUiThread(() -> {
                        try {
                            adapter.updateItems(menuItems);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onAddToCart(MenuItem item) {
        // Item added to cart - callback can be used for additional actions
    }
}

