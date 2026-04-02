package com.example.projectyear.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room Database for the Café Ordering App
 * Contains users, menu_items, orders, and order_items tables
 */
@Database(
    entities = {User.class, MenuItem.class, Order.class, OrderItem.class},
    version = 1,
    exportSchema = false
)
public abstract class CafeDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract MenuDao menuDao();
    public abstract OrderDao orderDao();

    private static volatile CafeDatabase instance;

    /**
     * Get singleton instance of CafeDatabase
     */
    public static CafeDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (CafeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CafeDatabase.class,
                            "cafe_database"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return instance;
    }
}

