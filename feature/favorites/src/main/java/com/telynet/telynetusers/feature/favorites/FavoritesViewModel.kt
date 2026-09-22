package com.telynet.telynetusers.feature.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telynet.telynetusers.core.domain.usecase.GetFavoritesUseCase
import com.telynet.telynetusers.core.domain.usecase.ToggleFavoriteUseCase
import com.telynet.telynetusers.core.models.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

        val uiState: StateFlow<FavoritesUiState> =
            getFavoritesUseCase
                .execute()
                .asFlow()
                .flowOn(Dispatchers.IO)
                .map<List<User>, FavoritesUiState> { users -> FavoritesUiState.Success(users) }
                .catch { error -> emit(FavoritesUiState.Error(error.localizedMessage ?: "Unknown error occurred")) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = FavoritesUiState.Loading,
                )

        // Removing a favorite updates the database; the favorites Flowable re-emits without the user
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
