package com.telynet.telynetusers.core.domain.usecase;

import com.telynet.telynetusers.core.domain.repository.UserRepository;
import com.telynet.telynetusers.core.models.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class GetUsersUseCase {
    private final UserRepository userRepository;

    @Inject
    public GetUsersUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flowable<List<User>> execute() {
        return userRepository.getUsers();
    }

    public Flowable<List<User>> execute(String searchQuery, int filterVisited, String orderBy) {
        return userRepository.getUsersFiltered(searchQuery, filterVisited, orderBy);
    }
}
