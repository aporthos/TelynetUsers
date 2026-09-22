package com.telynet.telynetusers.core.domain.repository;

import com.telynet.telynetusers.core.domain.entity.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface UserRepository {
    Flowable<List<User>> getUsers();

    Completable saveUser(User user);

    Completable deleteUser(User user);
}
