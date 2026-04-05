package com.example.projectyear.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectyear.database.CafeDatabase;
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

    public MenuViewModel(@NonNull Application application) {
        super(application);
        db = CafeDatabase.getInstance(application);
        seedMenuIfEmpty();
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
                menuItems.postValue(items);
            } catch (Exception e) {
                menuItems.postValue(new ArrayList<>());
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    public void loadAll() {
        loadMenu("All");
    }

    private void seedMenuIfEmpty() {
        executor.execute(() -> {
            try {
                List<MenuItem> existing = db.menuDao().getAllMenuItems();
                if (existing == null || existing.isEmpty()) {
                    insertSeedData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void insertSeedData() {
        // Coffee ☕
        db.menuDao().insertMenuItem(new MenuItem("Espresso", "Rich, bold shot of pure coffee", 150.0, "Coffee", 0));
        db.menuDao().insertMenuItem(new MenuItem("Café Latte", "Espresso with creamy steamed milk", 220.0, "Coffee", 0));
        db.menuDao().insertMenuItem(new MenuItem("Cappuccino", "Equal parts espresso, milk & foam", 230.0, "Coffee", 0));
        db.menuDao().insertMenuItem(new MenuItem("Americano", "Smooth espresso diluted with water", 190.0, "Coffee", 0));
        db.menuDao().insertMenuItem(new MenuItem("Mocha", "Chocolate-infused espresso with milk", 260.0, "Coffee", 0));
        db.menuDao().insertMenuItem(new MenuItem("Macchiato", "Espresso with a dash of foam", 200.0, "Coffee", 0));
        db.menuDao().insertMenuItem(new MenuItem("Iced Coffee", "Cold brewed coffee over ice", 240.0, "Coffee", 0));
        db.menuDao().insertMenuItem(new MenuItem("Flat White", "Velvety micro-foam over espresso", 250.0, "Coffee", 0));
        // Tea 🍵
        db.menuDao().insertMenuItem(new MenuItem("Green Tea", "Delicate, antioxidant-rich green tea", 160.0, "Tea", 0));
        db.menuDao().insertMenuItem(new MenuItem("Black Tea", "Classic bold Ceylon black tea", 150.0, "Tea", 0));
        db.menuDao().insertMenuItem(new MenuItem("Chamomile", "Soothing floral herbal infusion", 170.0, "Tea", 0));
        // Desserts 🍰
        db.menuDao().insertMenuItem(new MenuItem("Chocolate Brownie", "Warm fudgy brownie with nuts", 180.0, "Desserts", 0));
        db.menuDao().insertMenuItem(new MenuItem("Butter Croissant", "Flaky golden French croissant", 140.0, "Desserts", 0));
        db.menuDao().insertMenuItem(new MenuItem("Cheesecake", "Creamy New York-style cheesecake", 200.0, "Desserts", 0));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
