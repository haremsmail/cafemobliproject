package com.example.projectyear.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Data Access Object for users
 */
@Dao
public interface UserDao {

    /**
     * Insert a user
     */
    @Insert
    long insertUser(User user);

    /**
     * Get user by email
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    /**
     * Get user by ID
     */
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(int userId);
}

