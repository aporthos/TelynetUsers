package com.telynet.telynetusers.core.domain.usecase;

import com.telynet.telynetusers.core.domain.repository.UserRepository;
import com.telynet.telynetusers.core.models.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class GetFavoritesUseCase {
    private final UserRepository userRepository;

    @Inject
    public GetFavoritesUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Emits the favorite users sorted by name, and again whenever a favorite is added or removed.
     */
    public Flowable<List<User>> execute() {
        return userRepository.getFavoriteUsers();
    }
}
