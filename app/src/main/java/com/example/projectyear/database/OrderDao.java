package com.example.projectyear.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object for orders
 */
@Dao
public interface OrderDao {

    /**
     * Insert an order
     */
    @Insert
    long insertOrder(Order order);

    /**
     * Insert order items
     */
    @Insert
    void insertOrderItem(OrderItem orderItem);

    /**
     * Get all orders for a user
     */
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    List<Order> getUserOrders(int userId);

    /**
     * Get order by ID
     */
    @Query("SELECT * FROM orders WHERE id = :orderId")
    Order getOrderById(int orderId);

    /**
     * Get items in an order
     */
    @Query("SELECT oi.* FROM order_items oi WHERE oi.orderId = :orderId")
    List<OrderItem> getOrderItems(int orderId);

    /**
     * Get menu item details for an order item
     */
    @Query("SELECT mi.* FROM menu_items mi " +
            "INNER JOIN order_items oi ON mi.id = oi.menuItemId " +
            "WHERE oi.orderId = :orderId")
    List<MenuItem> getOrderMenuItems(int orderId);
}

