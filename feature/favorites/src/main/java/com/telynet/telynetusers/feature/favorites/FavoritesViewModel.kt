package com.telynet.telynetusers.feature.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.telynet.telynetusers.core.domain.usecase.GetFavoritesUseCase
import com.telynet.telynetusers.core.domain.usecase.ToggleFavoriteUseCase
import com.telynet.telynetusers.core.models.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        getFavoritesUseCase: GetFavoritesUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    ) : ViewModel() {
        private val disposables = CompositeDisposable()

        val favorites: Flow<PagingData<User>> =
            getFavoritesUseCase
                .execute()
                .asFlow()
                .cachedIn(viewModelScope)

        fun onFavoriteClick(user: User) {
            disposables.add(
                toggleFavoriteUseCase
                    .execute(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {},
                        { error -> Log.e(TAG, "Could not update favorite for ${user.code}", error) },
                    ),
            )
        }

        override fun onCleared() {
            disposables.clear()
        }

        private companion object {
            const val TAG = "FavoritesViewModel"
        }
    }
