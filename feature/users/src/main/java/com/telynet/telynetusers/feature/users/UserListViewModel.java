package com.telynet.telynetusers.feature.users;

import androidx.lifecycle.ViewModel;
import com.telynet.telynetusers.core.domain.usecase.GetUsersUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
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

    @Inject
    public UserListViewModel(GetUsersUseCase getUsersUseCase) {
        this.getUsersUseCase = getUsersUseCase;
        this._uiState = StateFlowKt.MutableStateFlow(UserListUiState.Loading.INSTANCE);
        this.uiState = _uiState;
        loadUsers();
    }

    public StateFlow<UserListUiState> getUiState() {
        return uiState;
    }

    private void loadUsers() {
        _uiState.setValue(UserListUiState.Loading.INSTANCE);
        Disposable disposable = getUsersUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userList -> _uiState.setValue(new UserListUiState.Success(userList)),
                        error -> {
                            String message = error.getLocalizedMessage() != null ? error.getLocalizedMessage() : "Unknown error occurred";
                            _uiState.setValue(new UserListUiState.Error(message));
                        }
                );
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
