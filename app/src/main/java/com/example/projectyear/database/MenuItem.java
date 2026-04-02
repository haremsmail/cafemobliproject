package com.example.projectyear.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * MenuItem entity for Room database
 * Stores café menu items with details
 */
@Entity(tableName = "menu_items")
public class MenuItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;
    public double price;
    public String category; // "Coffee", "Tea", "Desserts"
    public int imageResource; // Resource ID for the image
    public boolean available;

    public MenuItem(String name, String description, double price, String category, int imageResource) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageResource = imageResource;
        this.available = true;
    }
}

