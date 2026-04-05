package com.example.projectyear.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * OrderItem entity for Room database
 * Junction table linking orders to menu items with quantities
 */
@Entity(tableName = "order_items",
    indices = {
        @Index("orderId"),
        @Index("menuItemId")
    },
    foreignKeys = {
        @ForeignKey(
            entity = Order.class,
            parentColumns = "id",
            childColumns = "orderId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = MenuItem.class,
            parentColumns = "id",
            childColumns = "menuItemId",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int orderId;
    public int menuItemId;
    public int quantity;
    public double itemPrice; // Price at time of order

    public OrderItem(int orderId, int menuItemId, int quantity, double itemPrice) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }
}

