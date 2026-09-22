package com.telynet.telynetusers.feature.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.telynet.telynetusers.core.domain.usecase.GetUserByCodeUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class UserDetailViewModel extends ViewModel {

    private final GetUserByCodeUseCase getUserByCodeUseCase;
    private final MutableLiveData<UserDetailUiState> _uiState =
            new MutableLiveData<>(UserDetailUiState.Loading.INSTANCE);
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public UserDetailViewModel(GetUserByCodeUseCase getUserByCodeUseCase, SavedStateHandle savedStateHandle) {
        this.getUserByCodeUseCase = getUserByCodeUseCase;
        String code = savedStateHandle.get(UserDetailActivity.EXTRA_USER_CODE);
        loadUser(code);
    }

    public LiveData<UserDetailUiState> getUiState() {
        return _uiState;
    }

    private void loadUser(String code) {
        if (code == null || code.isEmpty()) {
            _uiState.setValue(UserDetailUiState.NotFound.INSTANCE);
            return;
        }
        disposables.add(getUserByCodeUseCase.execute(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> _uiState.setValue(new UserDetailUiState.Success(user)),
                        error -> _uiState.setValue(new UserDetailUiState.Error(error.getLocalizedMessage())),
                        () -> _uiState.setValue(UserDetailUiState.NotFound.INSTANCE)
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
