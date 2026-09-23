package com.telynet.telynetusers.core.data.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.telynet.telynetusers.core.database.dao.UserDao;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.TestObserver;

public class UserRepositoryImplTest {
    private UserDao userDao;
    private UserRepositoryImpl repository;

    @Before
    public void setUp() {
        userDao = mock(UserDao.class);
        repository = new UserRepositoryImpl(userDao);
    }

    @Test
    public void updateFavorite_markAsFavorite_delegatesCodeAndFlagToDao() {
        // Given
        String code = "U001";
        when(userDao.updateFavorite(code, true)).thenReturn(Completable.complete());

        // When
        TestObserver<Void> observer = repository.updateFavorite(code, true).test();

        // Then
        observer.assertComplete();
        verify(userDao).updateFavorite(code, true);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    public void updateFavorite_unmarkAsFavorite_delegatesCodeAndFlagToDao() {
        // Given
        String code = "U002";
        when(userDao.updateFavorite(code, false)).thenReturn(Completable.complete());

        // When
        TestObserver<Void> observer = repository.updateFavorite(code, false).test();

        // Then
        observer.assertComplete();
        verify(userDao).updateFavorite(code, false);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    public void updateFavorite_whenDaoFails_propagatesError() {
        // Given
        IllegalStateException error = new IllegalStateException("db error");
        when(userDao.updateFavorite(anyString(), anyBoolean())).thenReturn(Completable.error(error));

        // When
        TestObserver<Void> observer = repository.updateFavorite("U001", true).test();

        // Then
        observer.assertError(error).assertNotComplete();
    }

    @Test
    public void updateFavorite_doesNotRunDaoUpdateUntilSubscribed() {
        // Given
        AtomicBoolean executed = new AtomicBoolean(false);
        when(userDao.updateFavorite(anyString(), anyBoolean()))
                .thenReturn(Completable.fromAction(() -> executed.set(true)));

        // When
        Completable result = repository.updateFavorite("U001", true);

        // Then
        assertFalse(executed.get());
        result.test().assertComplete();
        assertTrue(executed.get());
    }
}
