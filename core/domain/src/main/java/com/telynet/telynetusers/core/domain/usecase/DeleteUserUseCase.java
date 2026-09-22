package com.telynet.telynetusers.core.domain.usecase;

import com.telynet.telynetusers.core.domain.entity.User;
import com.telynet.telynetusers.core.domain.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class DeleteUserUseCase {
    private final UserRepository userRepository;

    @Inject
    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Completable execute(User user) {
        return userRepository.deleteUser(user);
    }
}
