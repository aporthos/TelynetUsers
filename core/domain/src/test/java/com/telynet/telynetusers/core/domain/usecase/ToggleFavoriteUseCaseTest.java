package com.telynet.telynetusers.core.domain.usecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.telynet.telynetusers.core.domain.repository.UserRepository;
import com.telynet.telynetusers.core.models.entity.User;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.TestObserver;

public class ToggleFavoriteUseCaseTest {
    private UserRepository userRepository;
    private ToggleFavoriteUseCase useCase;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        useCase = new ToggleFavoriteUseCase(userRepository);
    }

    @Test
    public void execute_whenUserIsNotFavorite_marksAsFavorite() {
        // Given
        User user = user("U001", false);
        when(userRepository.updateFavorite("U001", true)).thenReturn(Completable.complete());

        // When
        TestObserver<Void> observer = useCase.execute(user).test();

        // Then
        observer.assertComplete();
        verify(userRepository).updateFavorite("U001", true);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void execute_whenUserIsFavorite_unmarksAsFavorite() {
        // Given
        User user = user("U002", true);
        when(userRepository.updateFavorite("U002", false)).thenReturn(Completable.complete());

        // When
        TestObserver<Void> observer = useCase.execute(user).test();

        // Then
        observer.assertComplete();
        verify(userRepository).updateFavorite("U002", false);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void execute_whenRepositoryFails_propagatesError() {
        // Given
        IllegalStateException error = new IllegalStateException("db error");
        when(userRepository.updateFavorite(anyString(), anyBoolean())).thenReturn(Completable.error(error));

        // When
        TestObserver<Void> observer = useCase.execute(user("U001", false)).test();

        // Then
        observer.assertError(error).assertNotComplete();
    }

    @Test
    public void execute_doesNotRunUpdateUntilSubscribed() {
        // Given
        AtomicBoolean executed = new AtomicBoolean(false);
        when(userRepository.updateFavorite(anyString(), anyBoolean()))
                .thenReturn(Completable.fromAction(() -> executed.set(true)));

        // When
        Completable result = useCase.execute(user("U001", false));

        // Then
        assertFalse(executed.get());
        result.test().assertComplete();
        assertTrue(executed.get());
    }

    private static User user(String code, boolean isFavorite) {
        return new User(code, "Name", "mail@test.com", "600000000", false,
                "Address", "https://image.test/1.png", "Company", isFavorite);
    }
}
