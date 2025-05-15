package com.example.mehrabi_task_java_view_based.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "full_name")
    public String fullName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "registration_timestamp")
    public long registrationTimestamp;

    public User(String fullName, String email, String password, long registrationTimestamp) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.registrationTimestamp = registrationTimestamp;
    }
}
