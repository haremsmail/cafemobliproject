package com.example.projectyear.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * User entity for Room database
 * Stores user login and account information
 */
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String email;
    public String passwordHash;
    public long createdAt;

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = System.currentTimeMillis();
    }
}

