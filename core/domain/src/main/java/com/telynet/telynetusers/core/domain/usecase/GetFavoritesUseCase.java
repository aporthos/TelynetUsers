package com.telynet.telynetusers.core.domain.usecase;

import androidx.paging.PagingData;

import com.telynet.telynetusers.core.domain.repository.UserRepository;
import com.telynet.telynetusers.core.models.entity.User;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class GetFavoritesUseCase {
    private final UserRepository userRepository;

    @Inject
    public GetFavoritesUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flowable<PagingData<User>> execute() {
        return userRepository.getFavoriteUsers();
    }
}
