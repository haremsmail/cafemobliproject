package com.example.projectyear;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectyear.viewmodels.AuthViewModel;
import com.google.android.material.button.MaterialButton;

public class TableSelectionActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private GridLayout gridTables;
    private TextView tvWelcome;
    private MaterialButton btnLogout;
    private int selectedTableNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_selection);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        tvWelcome = findViewById(R.id.tv_welcome);
        gridTables = findViewById(R.id.grid_tables);
        btnLogout = findViewById(R.id.btn_logout);

        // Set welcome message
        String email = authViewModel.getCurrentEmail();
        String name = email != null && email.contains("@") ?
            email.substring(0, email.indexOf('@')) : email;
        tvWelcome.setText("Welcome " + name + "@gmail.com");

        // Create table buttons (1-12)
        createTableButtons();

        // Logout button
        btnLogout.setOnClickListener(v -> {
            authViewModel.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void createTableButtons() {
        for (int i = 1; i <= 12; i++) {
            MaterialButton btnTable = new MaterialButton(this);
            btnTable.setText(i + "\n✓ FREE");
            btnTable.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline6);
            btnTable.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            btnTable.setTextColor(getResources().getColor(android.R.color.white));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 120;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);
            btnTable.setLayoutParams(params);

            final int tableNumber = i;
            btnTable.setOnClickListener(v -> openTableMenu(tableNumber));

            gridTables.addView(btnTable);
        }
    }

    private void openTableMenu(int tableNumber) {
        selectedTableNumber = tableNumber;
        // Pass table number to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TABLE_NUMBER", tableNumber);
        startActivity(intent);
    }
}

