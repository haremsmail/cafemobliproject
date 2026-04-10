package com.example.projectyear.viewmodels;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectyear.database.CafeDatabase;
import com.example.projectyear.database.Order;
import com.example.projectyear.database.OrderItem;
import com.example.projectyear.models.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderViewModel extends AndroidViewModel {

    private static final String TAG = "OrderViewModel";
    private final MutableLiveData<List<Order>> orderHistory = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Long> lastOrderId = new MutableLiveData<>(-1L);
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CafeDatabase db;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        db = CafeDatabase.getInstance(application);
    }

    public LiveData<List<Order>> getOrderHistory() { return orderHistory; }
    public LiveData<Long> getLastOrderId() { return lastOrderId; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void placeOrder(int userId, int tableNumber) {
        if (userId <= 0) {
            Log.e(TAG, "Invalid userId: " + userId);
            errorMessage.setValue("Error: User session expired. Please login again.");
            return;
        }

        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        executor.execute(() -> {
            try {
                // Verify user actually exists in db (handles SharedPreferences recovery ghost sessions)
                if (db.userDao().getUserById(userId) == null) {
                    throw new Exception("Ghost session: User does not exist in local database. Please log out and back in.");
                }

                // Run in a transaction to ensure both order and items are saved
                db.runInTransaction(() -> {
                    double totalPrice = Cart.getTotal();
                    Order order = new Order(userId, tableNumber, "confirmed", totalPrice);
                    long orderId = db.orderDao().insertOrder(order);

                    // Save order items
                    for (Cart.CartItem cartItem : Cart.getAllItems().values()) {
                        OrderItem orderItem = new OrderItem(
                            (int) orderId, cartItem.menuItemId, cartItem.quantity, cartItem.price
                        );
                        db.orderDao().insertOrderItem(orderItem);
                    }
                    
                    Log.d(TAG, "Order placed successfully. ID: " + orderId);
                    lastOrderId.postValue(orderId);
                });
            } catch (Exception e) {
                Log.e(TAG, "Failed to place order", e);
                errorMessage.postValue("Failed to place order: " + e.getMessage());
                lastOrderId.postValue(-1L);
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    public void resetLastOrderId() {
        lastOrderId.setValue(-1L);
    }

    public void clearError() {
        errorMessage.setValue(null);
    }

    public void loadOrderHistory(int userId) {
        if (userId <= 0) return;
        
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                List<Order> orders = db.orderDao().getUserOrders(userId);
                orderHistory.postValue(orders != null ? orders : new ArrayList<>());
            } catch (Exception e) {
                Log.e(TAG, "Error loading history", e);
                orderHistory.postValue(new ArrayList<>());
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
