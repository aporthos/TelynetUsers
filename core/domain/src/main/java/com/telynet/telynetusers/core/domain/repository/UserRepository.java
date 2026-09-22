package com.telynet.telynetusers.core.domain.repository;

import com.telynet.telynetusers.core.models.entity.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public interface UserRepository {
    Flowable<List<User>> getUsers();

    Flowable<List<User>> getUsersFiltered(String searchQuery, int filterVisited, String orderBy);

    Maybe<User> getUserByCode(String code);

    Completable saveUser(User user);

    Completable deleteUser(User user);
}
