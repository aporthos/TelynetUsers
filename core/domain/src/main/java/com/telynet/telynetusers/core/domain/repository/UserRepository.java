package com.telynet.telynetusers.core.domain.repository;

import androidx.paging.PagingData;

import com.telynet.telynetusers.core.models.entity.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public interface UserRepository {
    Flowable<List<User>> getUsers();

    Flowable<PagingData<User>> getUsersFiltered(String searchQuery, int filterVisited, String orderBy);

    Flowable<PagingData<User>> getFavoriteUsers();

    Maybe<User> getUserByCode(String code);

    Completable updateFavorite(String code, boolean isFavorite);

    Completable saveUser(User user);

    Completable deleteUser(User user);
}
