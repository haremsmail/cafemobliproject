package com.example.projectyear.viewmodels;

import android.app.Application;
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

    private final MutableLiveData<List<Order>> orderHistory = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Long> lastOrderId = new MutableLiveData<>(-1L);
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CafeDatabase db;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        db = CafeDatabase.getInstance(application);
    }

    public LiveData<List<Order>> getOrderHistory() { return orderHistory; }
    public LiveData<Long> getLastOrderId() { return lastOrderId; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void placeOrder(int userId) {
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                double totalPrice = Cart.getTotal();
                Order order = new Order(userId, "confirmed", totalPrice);
                long orderId = db.orderDao().insertOrder(order);

                // Save order items
                for (Cart.CartItem cartItem : Cart.getAllItems().values()) {
                    OrderItem orderItem = new OrderItem(
                        (int) orderId, cartItem.menuItemId, cartItem.quantity, cartItem.price
                    );
                    db.orderDao().insertOrderItem(orderItem);
                }

                lastOrderId.postValue(orderId);
            } catch (Exception e) {
                e.printStackTrace();
                lastOrderId.postValue(-1L);
            } finally {
                isLoading.postValue(false);
            }
        });
    }

    public void loadOrderHistory(int userId) {
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                List<Order> orders = db.orderDao().getUserOrders(userId);
                orderHistory.postValue(orders != null ? orders : new ArrayList<>());
            } catch (Exception e) {
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
