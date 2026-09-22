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
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.rx3.await
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        getFavoritesUseCase: GetFavoritesUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    ) : ViewModel() {
        val favorites: Flow<PagingData<User>> =
            getFavoritesUseCase
                .execute()
                .asFlow()
                .cachedIn(viewModelScope)

        private val _effects = Channel<FavoritesEffect>(Channel.BUFFERED)
        val effects: Flow<FavoritesEffect> = _effects.receiveAsFlow()

        fun onIntent(intent: FavoritesIntent) {
            when (intent) {
                is FavoritesIntent.ToggleFavorite -> toggleFavorite(intent.user)
            }
        }

        private fun toggleFavorite(user: User) {
            viewModelScope.launch {
                try {
                    toggleFavoriteUseCase.execute(user).subscribeOn(Schedulers.io()).await()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.e(TAG, "Could not update favorite for ${user.code}", e)
                    _effects.send(FavoritesEffect.ShowMessage("Couldn't update favorite for ${user.name}"))
                }
            }
        }

        private companion object {
            const val TAG = "FavoritesViewModel"
        }
    }
