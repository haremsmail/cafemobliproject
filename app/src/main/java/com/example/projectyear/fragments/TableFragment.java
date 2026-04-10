package com.example.projectyear.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectyear.MainActivity;
import com.example.projectyear.R;
import com.example.projectyear.adapters.TableAdapter;
import com.google.android.material.button.MaterialButton;

/**
 * Fragment for selecting a café table.
 * Users can pick a table which will be saved in SharedPreferences.
 */
public class TableFragment extends Fragment {

    private static final int TOTAL_TABLES = 12;
    private SharedPreferences prefs;
    private TextView tvCurrentTable;
    private MaterialButton btnGoToMenu;
    private int selectedTable = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireActivity().getSharedPreferences("cafe_app", Context.MODE_PRIVATE);
        tvCurrentTable = view.findViewById(R.id.tv_current_table);
        btnGoToMenu = view.findViewById(R.id.btn_go_to_menu);
        RecyclerView rvTables = view.findViewById(R.id.rv_tables);

        // Load current selection if any
        selectedTable = prefs.getInt("selected_table", -1);
        updateStatusText();

        rvTables.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        TableAdapter adapter = new TableAdapter(requireContext(), TOTAL_TABLES, tableNumber -> {
            selectedTable = tableNumber;
            btnGoToMenu.setEnabled(true);
            updateStatusText();
            
            // Auto-save selection
            prefs.edit().putInt("selected_table", selectedTable).apply();
        });
        rvTables.setAdapter(adapter);

        if (selectedTable > 0) {
            btnGoToMenu.setEnabled(true);
        }

        btnGoToMenu.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).navigateTo(R.id.nav_menu);
            }
        });
    }

    private void updateStatusText() {
        if (selectedTable > 0) {
            tvCurrentTable.setText(getString(R.string.table_current_label, selectedTable));
        } else {
            tvCurrentTable.setText(getString(R.string.table_none_selected));
        }
    }
}
