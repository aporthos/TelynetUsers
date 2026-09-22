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
    @Provides
    @Singleton
    public static AppDatabase provideDatabase(@ApplicationContext Context context, Provider<AppDatabase> databaseProvider) {
        return Room.databaseBuilder(context, AppDatabase.class, "telynet_users_db")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            UserDao userDao = databaseProvider.get().userDao();

                            List<UserEntity> fakeUsers = FakeDataGenerator.generate200FakeUsers();
                            userDao.insertAll(fakeUsers);
                        });
                    }
                })
                .build();
    }

    @Provides
    @Singleton
    public static UserDao provideUserDao(AppDatabase database) {
        return database.userDao();
    }
}
