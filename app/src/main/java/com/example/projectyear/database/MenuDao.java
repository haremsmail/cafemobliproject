package com.example.projectyear.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for menu items
 */
@Dao
public interface MenuDao {

    /**
     * Insert a menu item
     */
    @Insert
    void insertMenuItem(MenuItem menuItem);

    /**
     * Get all menu items
     */
    @Query("SELECT * FROM menu_items WHERE available = 1 ORDER BY category, name")
    List<MenuItem> getAllMenuItems();

    /**
     * Get menu items by category
     */
    @Query("SELECT * FROM menu_items WHERE category = :category AND available = 1")
    List<MenuItem> getMenuItemsByCategory(String category);

    /**
     * Get menu item by ID
     */
    @Query("SELECT * FROM menu_items WHERE id = :itemId")
    MenuItem getMenuItemById(int itemId);

    /**
     * Update menu item
     */
    @Update
    void updateMenuItem(MenuItem menuItem);

    /**
     * Delete all menu items (for testing/reset)
     */
    @Query("DELETE FROM menu_items")
    void deleteAllMenuItems();

    /**
     * Get count of all menu items
     */
    @Query("SELECT COUNT(*) FROM menu_items")
    int getMenuItemCount();
}

