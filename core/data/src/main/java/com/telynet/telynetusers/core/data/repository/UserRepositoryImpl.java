package com.telynet.telynetusers.core.data.repository;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataTransforms;
import androidx.paging.PagingSource;
import androidx.paging.rxjava3.PagingRx;

import com.telynet.telynetusers.core.database.dao.UserDao;
import com.telynet.telynetusers.core.database.entity.UserEntity;
import com.telynet.telynetusers.core.domain.repository.UserRepository;
import com.telynet.telynetusers.core.models.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public class UserRepositoryImpl implements UserRepository {
    private static final int PAGE_SIZE = 20;
    private static final Executor MAPPING_EXECUTOR = Runnable::run;

    private final UserDao userDao;

    @Inject
    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Flowable<List<User>> getUsers() {
        return userDao.getAllUsers().map(entities -> {
            List<User> users = new ArrayList<>();
            for (UserEntity entity : entities) {
                users.add(mapEntityToUser(entity));
            }
            return users;
        });
    }

    @Override
    public Flowable<PagingData<User>> getUsersFiltered(String searchQuery, int filterVisited, String orderBy) {
        return pagedUsers(() -> userDao.getUsersFiltered(searchQuery, filterVisited, orderBy));
    }

    @Override
    public Flowable<PagingData<User>> getFavoriteUsers() {
        return pagedUsers(userDao::getFavoriteUsers);
    }

    @Override
    public Maybe<User> getUserByCode(String code) {
        return userDao.getUserByCode(code).flatMapMaybe(entities -> entities.isEmpty()
                ? Maybe.empty()
                : Maybe.just(mapEntityToUser(entities.get(0))));
    }

    @Override
    public Completable updateFavorite(String code, boolean isFavorite) {
        return userDao.updateFavorite(code, isFavorite);
    }

    @Override
    public Completable saveUser(User user) {
        return userDao.insertUser(mapUserToEntity(user));
    }

    @Override
    public Completable deleteUser(User user) {
        return userDao.deleteUser(mapUserToEntity(user));
    }

    private Flowable<PagingData<User>> pagedUsers(kotlin.jvm.functions.Function0<PagingSource<Integer, UserEntity>> pagingSourceFactory) {
        Pager<Integer, UserEntity> pager = new Pager<>(
                new PagingConfig(PAGE_SIZE, PAGE_SIZE, false, PAGE_SIZE),
                null,
                pagingSourceFactory
        );
        return PagingRx.getFlowable(pager)
                .map(pagingData -> PagingDataTransforms.map(pagingData, MAPPING_EXECUTOR, this::mapEntityToUser));
    }

    private User mapEntityToUser(UserEntity entity) {
        return new User(
                entity.getCode(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.isVisited(),
                entity.getAddress(),
                entity.getImageUrl(),
                entity.getCompany(),
                entity.isFavorite()
        );
    }

    private UserEntity mapUserToEntity(User user) {
        return new UserEntity(
                user.getCode(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.isVisited(),
                user.getAddress(),
                user.getImageUrl(),
                user.getCompany(),
                user.isFavorite()
        );
    }
}
