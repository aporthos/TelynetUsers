package com.telynet.telynetusers.core.domain.usecase;

import com.telynet.telynetusers.core.domain.repository.UserRepository;
import com.telynet.telynetusers.core.models.entity.User;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Maybe;

public class GetUserByCodeUseCase {
    private final UserRepository userRepository;

    @Inject
    public GetUserByCodeUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Maybe<User> execute(String code) {
        return userRepository.getUserByCode(code);
    }
}
