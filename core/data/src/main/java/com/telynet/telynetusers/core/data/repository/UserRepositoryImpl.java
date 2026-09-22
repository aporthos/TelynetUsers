package com.telynet.telynetusers.core.data.repository;

import com.telynet.telynetusers.core.database.dao.UserDao;
import com.telynet.telynetusers.core.database.entity.UserEntity;
import com.telynet.telynetusers.core.domain.entity.User;
import com.telynet.telynetusers.core.domain.repository.UserRepository;

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
                users.add(new User(entity.getId(), entity.getName(), entity.getEmail()));
            }
            return users;
        });
    }

    @Override
    public Completable saveUser(User user) {
        return userDao.insertUser(new UserEntity(user.getId(), user.getName(), user.getEmail()));
    }

    @Override
    public Completable deleteUser(User user) {
        return userDao.deleteUser(new UserEntity(user.getId(), user.getName(), user.getEmail()));
    }
}
