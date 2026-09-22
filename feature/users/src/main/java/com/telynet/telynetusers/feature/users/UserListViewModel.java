package com.telynet.telynetusers.feature.users;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.CachedPagingDataKt;
import androidx.paging.PagingData;

import com.telynet.telynetusers.core.domain.usecase.GetUsersUseCase;
import com.telynet.telynetusers.core.domain.usecase.ToggleFavoriteUseCase;
import com.telynet.telynetusers.core.models.entity.User;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import kotlinx.coroutines.reactive.ReactiveFlowKt;

@HiltViewModel
public class UserListViewModel extends ViewModel {

    private static final String TAG = "UserListViewModel";

    private final GetUsersUseCase getUsersUseCase;
    private final ToggleFavoriteUseCase toggleFavoriteUseCase;
    private final MutableStateFlow<UserListUiState> _uiState;
    private final StateFlow<UserListUiState> uiState;

    private final BehaviorSubject<UserListUiState> queries;
    private final Flow<PagingData<User>> users;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public UserListViewModel(GetUsersUseCase getUsersUseCase, ToggleFavoriteUseCase toggleFavoriteUseCase) {
        this.getUsersUseCase = getUsersUseCase;
        this.toggleFavoriteUseCase = toggleFavoriteUseCase;
        this._uiState = StateFlowKt.MutableStateFlow(UserListUiState.initial());
        this.uiState = _uiState;
        this.queries = BehaviorSubject.createDefault(_uiState.getValue());

        Flowable<PagingData<User>> pagedUsers = queries
                .toFlowable(BackpressureStrategy.LATEST)
                .distinctUntilChanged(UserListUiState::hasSameQueryAs)
                .switchMap(query -> getUsersUseCase.execute(
                        query.getSearchQuery(), query.getFilterVisited(), query.getOrderBy()));

        this.users = CachedPagingDataKt.cachedIn(
                ReactiveFlowKt.asFlow(pagedUsers),
                ViewModelKt.getViewModelScope(this));

        loadCounts();
    }

    public StateFlow<UserListUiState> getUiState() {
        return uiState;
    }

    public Flow<PagingData<User>> getUsers() {
        return users;
    }

    public void processIntent(UserListIntent intent) {
        UserListUiState currentState = _uiState.getValue();
        if (intent instanceof UserListIntent.SearchQueryChanged) {
            updateQuery(currentState.copyWith(((UserListIntent.SearchQueryChanged) intent).getQuery(), null, null));
        } else if (intent instanceof UserListIntent.FilterVisitedChanged) {
            updateQuery(currentState.copyWith(null, ((UserListIntent.FilterVisitedChanged) intent).getFilter(), null));
        } else if (intent instanceof UserListIntent.OrderByChanged) {
            updateQuery(currentState.copyWith(null, null, ((UserListIntent.OrderByChanged) intent).getOrderBy()));
        } else if (intent instanceof UserListIntent.ToggleFavorite) {
            toggleFavorite(((UserListIntent.ToggleFavorite) intent).getUser());
        }
    }

    private void updateQuery(UserListUiState newState) {
        _uiState.setValue(newState);
        queries.onNext(newState);
    }

    // Room invalidates the PagingSource after the update, so the visible page refreshes on its own
    private void toggleFavorite(User user) {
        disposables.add(toggleFavoriteUseCase.execute(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                        },
                        error -> Log.e(TAG, "Could not update favorite for " + user.getCode(), error)
                ));
    }

    private void loadCounts() {
        disposables.add(getUsersUseCase.execute()
                .map(users -> {
                    int visited = 0;
                    for (User user : users) {
                        if (user.isVisited()) visited++;
                    }
                    return new int[]{users.size(), visited};
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        counts -> _uiState.setValue(_uiState.getValue().withCounts(counts[0], counts[1])),
                        error -> {

                        }
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
