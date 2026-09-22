package com.telynet.telynetusers.core.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.telynet.telynetusers.core.database.dao.UserDao;
import com.telynet.telynetusers.core.database.entity.UserEntity;

@Database(entities = {UserEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
