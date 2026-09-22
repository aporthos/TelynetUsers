package com.telynet.telynetusers.feature.users;

import androidx.lifecycle.ViewModel;

import com.telynet.telynetusers.core.domain.usecase.GetUsersUseCase;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.telynet.telynetusers.core.models.entity.User;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

import javax.inject.Inject;

@HiltViewModel
public class UserListViewModel extends ViewModel {

    private final GetUsersUseCase getUsersUseCase;
    private final MutableStateFlow<UserListUiState> _uiState;
    private final StateFlow<UserListUiState> uiState;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Disposable activeDisposable;

    @Inject
    public UserListViewModel(GetUsersUseCase getUsersUseCase) {
        this.getUsersUseCase = getUsersUseCase;
        this._uiState = StateFlowKt.MutableStateFlow(UserListUiState.initial());
        this.uiState = _uiState;
        loadCounts();
        loadUsers();
    }

    public StateFlow<UserListUiState> getUiState() {
        return uiState;
    }

    public void processIntent(UserListIntent intent) {
        UserListUiState currentState = _uiState.getValue();
        if (intent instanceof UserListIntent.SearchQueryChanged) {
            _uiState.setValue(currentState.copyWith(null, ((UserListIntent.SearchQueryChanged) intent).getQuery(), null, null));
            loadUsers();
        } else if (intent instanceof UserListIntent.FilterVisitedChanged) {
            _uiState.setValue(currentState.copyWith(null, null, ((UserListIntent.FilterVisitedChanged) intent).getFilter(), null));
            loadUsers();
        } else if (intent instanceof UserListIntent.OrderByChanged) {
            _uiState.setValue(currentState.copyWith(null, null, null, ((UserListIntent.OrderByChanged) intent).getOrderBy()));
            loadUsers();
        }
    }

    private void loadUsers() {
        if (activeDisposable != null && !activeDisposable.isDisposed()) {
            disposables.remove(activeDisposable);
        }
        UserListUiState currentState = _uiState.getValue();
        if (!(currentState.getResult() instanceof UserListUiState.Result.Success)) {
            _uiState.setValue(currentState.copyWith(UserListUiState.Result.Loading.INSTANCE, null, null, null));
        }

        activeDisposable = getUsersUseCase.execute(currentState.getSearchQuery(), currentState.getFilterVisited(), currentState.getOrderBy())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userList -> {
                            UserListUiState latestState = _uiState.getValue();
                            _uiState.setValue(latestState.copyWith(new UserListUiState.Result.Success(userList), null, null, null));
                        },
                        error -> {
                            UserListUiState latestState = _uiState.getValue();
                            String message = error.getLocalizedMessage() != null ? error.getLocalizedMessage() : "Unknown error occurred";
                            _uiState.setValue(latestState.copyWith(new UserListUiState.Result.Error(message), null, null, null));
                        }
                );
        disposables.add(activeDisposable);
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
