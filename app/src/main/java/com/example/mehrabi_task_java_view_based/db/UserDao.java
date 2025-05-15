package com.example.mehrabi_task_java_view_based.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);
}
