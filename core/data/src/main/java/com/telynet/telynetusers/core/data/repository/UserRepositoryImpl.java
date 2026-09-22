package com.telynet.telynetusers.core.data.repository;

import com.telynet.telynetusers.core.database.dao.UserDao;
import com.telynet.telynetusers.core.database.entity.UserEntity;
import com.telynet.telynetusers.core.domain.repository.UserRepository;
import com.telynet.telynetusers.core.models.entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class UserRepositoryImpl implements UserRepository {
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
    public Flowable<List<User>> getUsersFiltered(String searchQuery, int filterVisited, String orderBy) {
        return userDao.getUsersFiltered(searchQuery, filterVisited, orderBy).map(entities -> {
            List<User> users = new ArrayList<>();
            for (UserEntity entity : entities) {
                users.add(mapEntityToUser(entity));
            }
            return users;
        });
    }

    @Override
    public Completable saveUser(User user) {
        return userDao.insertUser(mapUserToEntity(user));
    }

    @Override
    public Completable deleteUser(User user) {
        return userDao.deleteUser(mapUserToEntity(user));
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
                entity.getCompany()
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
                user.getCompany()
        );
    }
}
