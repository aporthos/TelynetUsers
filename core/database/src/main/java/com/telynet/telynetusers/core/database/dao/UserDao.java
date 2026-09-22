package com.telynet.telynetusers.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.telynet.telynetusers.core.database.entity.UserEntity;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    Flowable<List<UserEntity>> getAllUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(UserEntity user);

    @Delete
    Completable deleteUser(UserEntity user);
}
