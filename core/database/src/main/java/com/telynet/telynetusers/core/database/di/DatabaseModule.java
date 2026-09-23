package com.telynet.telynetusers.core.database.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.telynet.telynetusers.core.database.AppDatabase;
import com.telynet.telynetusers.core.database.FakeDataGenerator;
import com.telynet.telynetusers.core.database.dao.UserDao;
import com.telynet.telynetusers.core.database.entity.UserEntity;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    private static final String DATABASE_NAME = "telynet_users_db.db";

    @Provides
    @Singleton
    public static AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .createFromAsset("database/" + DATABASE_NAME)
                .build();
    }

    @Provides
    @Singleton
    public static UserDao provideUserDao(AppDatabase database) {
        return database.userDao();
    }
}
