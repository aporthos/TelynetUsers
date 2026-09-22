package com.telynet.telynetusers.feature.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.telynet.telynetusers.core.domain.usecase.GetUsersUseCase
import com.telynet.telynetusers.core.domain.usecase.ToggleFavoriteUseCase
import com.telynet.telynetusers.core.models.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.rx3.await
import javax.inject.Inject

@HiltViewModel
class UserListViewModel
    @Inject
    constructor(
        private val getUsersUseCase: GetUsersUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    ) : ViewModel() {
        private companion object {
            const val TAG = "UserListViewModel"
        }

        private val _uiState = MutableStateFlow(UserListUiState())
        val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

        private val _effects = Channel<UserListEffect>(Channel.BUFFERED)
        val effects: Flow<UserListEffect> = _effects.receiveAsFlow()

        @OptIn(ExperimentalCoroutinesApi::class)
        val users: Flow<PagingData<User>> =
            _uiState
                .map { it.query }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    getUsersUseCase
                        .execute(query.searchQuery, query.filter.value, query.sort.key)
                        .asFlow()
                }.cachedIn(viewModelScope)

        init {
            observeCounts()
        }

        fun onIntent(intent: UserListIntent) {
            when (intent) {
                is UserListIntent.ToggleFavorite -> toggleFavorite(intent.user)
                else -> _uiState.update { state -> reduce(state, intent) }
            }
        }

        private fun reduce(
            state: UserListUiState,
            intent: UserListIntent,
        ): UserListUiState =
            when (intent) {
                is UserListIntent.SearchQueryChanged -> state.copy(searchQuery = intent.query)
                is UserListIntent.FilterSelected -> state.copy(filter = intent.filter)
                is UserListIntent.SortSelected -> state.copy(sort = intent.sort)
                is UserListIntent.ToggleFavorite -> state
            }

        private fun toggleFavorite(user: User) {
            viewModelScope.launch {
                try {
                    toggleFavoriteUseCase.execute(user).subscribeOn(Schedulers.io()).await()
                } catch (e: CancellationException) {
                    Log.d(TAG, "User list cancelled", e)
                    throw e
                } catch (e: Exception) {
                    Log.e(TAG, "Could not update favorite for ${user.code}", e)
                    _effects.send(UserListEffect.ShowMessage("Couldn't update favorite for ${user.name}"))
                }
            }
        }

        private fun observeCounts() {
            getUsersUseCase
                .execute()
                .asFlow()
                .map { users -> users.size to users.count { it.isVisited } }
                .flowOn(Dispatchers.Default)
                .onEach { (total, visited) ->
                    _uiState.update { it.copy(totalCount = total, visitedCount = visited) }
                }.catch { error -> Log.e(TAG, "Could not load user counts", error) }
                .launchIn(viewModelScope)
        }
    }
