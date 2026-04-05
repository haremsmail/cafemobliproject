package com.example.projectyear.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectyear.models.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {

    private final MutableLiveData<List<Cart.CartItem>> cartItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> itemCount = new MutableLiveData<>(0);
    private final MutableLiveData<Double> subtotal = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> tax = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> total = new MutableLiveData<>(0.0);

    public LiveData<List<Cart.CartItem>> getCartItems() { return cartItems; }
    public LiveData<Integer> getItemCount() { return itemCount; }
    public LiveData<Double> getSubtotal() { return subtotal; }
    public LiveData<Double> getTax() { return tax; }
    public LiveData<Double> getTotal() { return total; }

    public void addItem(int menuItemId, String itemName, double price) {
        Cart.addItem(menuItemId, itemName, price);
        refreshCart();
    }

    public void removeItem(int menuItemId) {
        Cart.removeItem(menuItemId);
        refreshCart();
    }

    public void updateQuantity(int menuItemId, int quantity) {
        Cart.updateQuantity(menuItemId, quantity);
        refreshCart();
    }

    public void clearCart() {
        Cart.clear();
        refreshCart();
    }

    public void refreshCart() {
        List<Cart.CartItem> items = new ArrayList<>(Cart.getAllItems().values());
        cartItems.setValue(items);
        itemCount.setValue(getTotalItemCount());
        double sub = Cart.getSubtotal();
        double t = Cart.getTax();
        subtotal.setValue(sub);
        tax.setValue(t);
        total.setValue(sub + t);
    }

    private int getTotalItemCount() {
        int count = 0;
        for (Cart.CartItem item : Cart.getAllItems().values()) {
            count += item.quantity;
        }
        return count;
    }

    public boolean isCartEmpty() {
        return Cart.isEmpty();
    }
}
