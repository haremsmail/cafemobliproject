package com.example.projectyear.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectyear.database.CafeDatabase;
import com.example.projectyear.database.DatabaseHelper;
import com.example.projectyear.database.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuViewModel extends AndroidViewModel {

    private final MutableLiveData<List<MenuItem>> menuItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>("All");
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CafeDatabase db;
    private final Application application;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        db = CafeDatabase.getInstance(application);
        // Don't load here - let fragments initialize database first
    }

    public LiveData<List<MenuItem>> getMenuItems() { return menuItems; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getSelectedCategory() { return selectedCategory; }

    public void loadMenu(String category) {
        selectedCategory.setValue(category);
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                Thread.sleep(300); // brief delay for loading UX
                List<MenuItem> items;
                if (category == null || category.equals("All")) {
                    items = db.menuDao().getAllMenuItems();
                } else {
                    items = db.menuDao().getMenuItemsByCategory(category);
                }
                // Ensure list is not null
                if (items == null) {
                    items = new ArrayList<>();
                }
                menuItems.postValue(items);
            } catch (Exception e) {
                e.printStackTrace();
                menuItems.postValue(new ArrayList<>());
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    public void loadAll() {
        loadMenu("All");
    }

    /**
     * Manually reseed the database (useful for development)
     */
    public void reseedDatabase() {
        executor.execute(() -> {
            DatabaseHelper.reseedDatabase(application);
            // Reload menu after reseeding
            loadAll();
        });
    }

    /**
     * Clear all menu items (useful for testing)
     */
    public void clearMenuItems() {
        executor.execute(() -> {
            DatabaseHelper.clearMenuItems(application);
            menuItems.postValue(new ArrayList<>());
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
