package com.telynet.telynetusers.core.database.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.telynet.telynetusers.core.database.entity.UserEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    Flowable<List<UserEntity>> getAllUsers();

    @Query("SELECT * FROM users " +
           "WHERE (:searchQuery = '' OR name LIKE '%' || :searchQuery || '%') " +
           "AND (:filterVisited = -1 OR (:filterVisited = 1 AND isVisited = 1) OR (:filterVisited = 0 AND isVisited = 0)) " +
           "ORDER BY " +
           "CASE WHEN :orderBy = 'name' THEN name END ASC, " +
           "CASE WHEN :orderBy = 'name_desc' THEN name END DESC, " +
           "CASE WHEN :orderBy = 'code' THEN code END ASC, " +
           "CASE WHEN :orderBy = 'code_desc' THEN code END DESC, " +
           "code ASC")
    PagingSource<Integer, UserEntity> getUsersFiltered(String searchQuery, int filterVisited, String orderBy);

    @Query("SELECT * FROM users WHERE isFavorite = 1 ORDER BY name ASC")
    PagingSource<Integer, UserEntity> getFavoriteUsers();

    @Query("SELECT * FROM users WHERE code = :code LIMIT 1")
    Single<List<UserEntity>> getUserByCode(String code);

    @Query("UPDATE users SET isFavorite = :isFavorite WHERE code = :code")
    Completable updateFavorite(String code, boolean isFavorite);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(UserEntity user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserEntity> users);

    @Delete
    Completable deleteUser(UserEntity user);
}
