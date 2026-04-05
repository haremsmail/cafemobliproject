package com.example.projectyear.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Order entity for Room database
 * Stores customer orders with timestamps
 */
@Entity(tableName = "orders",
    indices = {@Index("userId")},
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
    )
)
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public long orderDate; // Timestamp in milliseconds
    public String status; // "pending", "confirmed", "ready"
    public double totalPrice;

    public Order(int userId, String status, double totalPrice) {
        this.userId = userId;
        this.orderDate = System.currentTimeMillis();
        this.status = status;
        this.totalPrice = totalPrice;
    }
}

