package com.example.projectyear.database;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Database Helper for initializing and managing the Cafe Database
 * Handles seeding of menu items and other initialization tasks
 */
public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private static final Object LOCK = new Object();
    private static boolean isInitialized = false;

    /**
     * Initialize the database with seed data
     */
    public static void initializeDatabase(Context context) {
        if (isInitialized) {
            Log.d(TAG, "Database already initialized");
            return;
        }

        synchronized (LOCK) {
            if (isInitialized) {
                return;
            }

            try {
                CafeDatabase db = CafeDatabase.getInstance(context);

                // Check if database has menu items already
                List<MenuItem> existingItems = db.menuDao().getAllMenuItems();

                if (existingItems == null || existingItems.isEmpty()) {
                    Log.d(TAG, "Database is empty, seeding menu items...");
                    seedMenuItems(db);
                    Log.d(TAG, "Database seeding completed successfully");

                    // Verify data was inserted
                    List<MenuItem> verifyItems = db.menuDao().getAllMenuItems();
                    Log.d(TAG, "Verification: Database now contains " + (verifyItems != null ? verifyItems.size() : 0) + " items");
                } else {
                    Log.d(TAG, "Database already has " + existingItems.size() + " menu items");
                }

                isInitialized = true;
            } catch (Exception e) {
                Log.e(TAG, "Error initializing database: " + e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Seed the database with coffee, tea, and dessert menu items
     */
    private static void seedMenuItems(CafeDatabase db) {
        List<MenuItem> items = new ArrayList<>();

        // ===== COFFEE ITEMS (☕) =====
        items.add(new MenuItem("Americano", "Smooth espresso diluted with water", 1500.0, "Coffee", 0));
        items.add(new MenuItem("Café Latte", "Espresso with creamy steamed milk", 2000.0, "Coffee", 0));
        items.add(new MenuItem("Cappuccino", "Equal parts espresso, milk & foam", 2000.0, "Coffee", 0));
        items.add(new MenuItem("Espresso", "Rich, bold shot of pure coffee", 1500.0, "Coffee", 0));
        items.add(new MenuItem("Flat White", "Velvety micro-foam over espresso", 2500.0, "Coffee", 0));
        items.add(new MenuItem("Iced Coffee", "Cold brewed coffee over ice", 2500.0, "Coffee", 0));
        items.add(new MenuItem("Macchiato", "Espresso with a dash of foam", 1800.0, "Coffee", 0));
        items.add(new MenuItem("Mocha", "Chocolate-infused espresso with milk", 2300.0, "Coffee", 0));

        // ===== TEA ITEMS (🍵) =====
        items.add(new MenuItem("Black Tea", "Classic bold Ceylon black tea", 1500.0, "Tea", 0));
        items.add(new MenuItem("Chamomile", "Soothing floral herbal infusion", 1500.0, "Tea", 0));
        items.add(new MenuItem("Green Tea", "Delicate, antioxidant-rich green tea", 1500.0, "Tea", 0));

        // ===== DESSERT ITEMS (🍰) =====
        items.add(new MenuItem("Butter Croissant", "Flaky golden French croissant", 1200.0, "Desserts", 0));
        items.add(new MenuItem("Cheesecake", "Creamy New York-style cheesecake", 1800.0, "Desserts", 0));
        items.add(new MenuItem("Chocolate Brownie", "Warm fudgy brownie with nuts", 1600.0, "Desserts", 0));

        // Insert all items into database
        for (MenuItem item : items) {
            db.menuDao().insertMenuItem(item);
            Log.d(TAG, "Inserted menu item: " + item.name + " (" + item.category + ") - IQD " + item.price);
        }

        Log.d(TAG, "Seeded " + items.size() + " menu items successfully");
    }

    /**
     * Clear all menu items from database (useful for testing)
     */
    public static void clearMenuItems(Context context) {
        try {
            CafeDatabase db = CafeDatabase.getInstance(context);
            db.menuDao().deleteAllMenuItems();
            Log.d(TAG, "Cleared all menu items from database");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing menu items: " + e.getMessage(), e);
        }
    }

    /**
     * Reseed the database with fresh data
     */
    public static void reseedDatabase(Context context) {
        synchronized (LOCK) {
            try {
                CafeDatabase db = CafeDatabase.getInstance(context);
                db.menuDao().deleteAllMenuItems();
                isInitialized = false;
                initializeDatabase(context);
                Log.d(TAG, "Database reseeded successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error reseeding database: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Get the total count of menu items
     */
    public static int getMenuItemCount(Context context) {
        try {
            CafeDatabase db = CafeDatabase.getInstance(context);
            List<MenuItem> items = db.menuDao().getAllMenuItems();
            return items != null ? items.size() : 0;
        } catch (Exception e) {
            Log.e(TAG, "Error getting menu item count: " + e.getMessage(), e);
            return 0;
        }
    }
}




