package com.example.projectyear.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Cart class for managing items in the current shopping session
 * Uses a static instance to maintain state across activities
 */
public class Cart {

    // Maps menuItemId to CartItem
    private static Map<Integer, CartItem> items = new HashMap<>();

    /**
     * Add or update item in cart
     */
    public static void addItem(int menuItemId, String itemName, double price) {
        if (items.containsKey(menuItemId)) {
            CartItem cartItem = items.get(menuItemId);
            cartItem.quantity++;
        } else {
            items.put(menuItemId, new CartItem(menuItemId, itemName, price, 1));
        }
    }

    /**
     * Remove item from cart
     */
    public static void removeItem(int menuItemId) {
        items.remove(menuItemId);
    }

    /**
     * Update item quantity
     */
    public static void updateQuantity(int menuItemId, int quantity) {
        if (items.containsKey(menuItemId)) {
            if (quantity <= 0) {
                items.remove(menuItemId);
            } else {
                items.get(menuItemId).quantity = quantity;
            }
        }
    }

    /**
     * Get all items in cart
     */
    public static Map<Integer, CartItem> getAllItems() {
        return items;
    }

    /**
     * Calculate subtotal
     */
    public static double getSubtotal() {
        double subtotal = 0;
        for (CartItem item : items.values()) {
            subtotal += item.price * item.quantity;
        }
        return subtotal;
    }

    /**
     * Calculate tax (5%)
     */
    public static double getTax() {
        return getSubtotal() * 0.05;
    }

    /**
     * Calculate total price
     */
    public static double getTotal() {
        return getSubtotal() + getTax();
    }

    /**
     * Get item count
     */
    public static int getItemCount() {
        return items.size();
    }

    /**
     * Check if cart is empty
     */
    public static boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Clear all items from cart
     */
    public static void clear() {
        items.clear();
    }

    /**
     * Inner class representing a single item in the cart
     */
    public static class CartItem {
        public int menuItemId;
        public String itemName;
        public double price;
        public int quantity;

        public CartItem(int menuItemId, String itemName, double price, int quantity) {
            this.menuItemId = menuItemId;
            this.itemName = itemName;
            this.price = price;
            this.quantity = quantity;
        }

        public double getTotalPrice() {
            return price * quantity;
        }
    }
}

