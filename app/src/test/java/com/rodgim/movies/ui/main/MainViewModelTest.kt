package com.rodgim.movies.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rodgim.movies.mockedMovie
import com.rodgim.movies.ui.home.MoviesViewModel
import com.rodgim.movies.ui.models.CategoryMovie
import com.rodgim.usecases.GetMoviesFromCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getMoviesFromCategory: GetMoviesFromCategory

    @Mock
    lateinit var observer: Observer<MoviesViewModel.UiModel>

    private lateinit var vm: MoviesViewModel

    @Before
    fun setUp() {
        vm = MoviesViewModel(getMoviesFromCategory, Dispatchers.Unconfined)
    }

    @Test
    fun `observing LiveData launches location permission request`() {
        vm.model.observeForever(observer)

        verify(observer).onChanged(MoviesViewModel.UiModel.RequestLocationPermission)
    }

    @Test
    fun `after requesting the permission, loading is shown`() {
        runBlocking {
            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(getMoviesFromCategory.invoke(CategoryMovie.POPULAR.id)).thenReturn(movies)
            vm.model.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(MoviesViewModel.UiModel.Loading)
        }
    }

    @Test
    fun `after requesting the permission, getPopularMovies is called`() {
        runBlocking {
            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(getMoviesFromCategory.invoke(CategoryMovie.POPULAR.id)).thenReturn(movies)
            whenever(getMoviesFromCategory.invoke(CategoryMovie.NOW_PLAYING.id)).thenReturn(movies)
            whenever(getMoviesFromCategory.invoke(CategoryMovie.TOP_RATED.id)).thenReturn(movies)

            vm.model.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(MoviesViewModel.UiModel.Content(movies, movies, movies))
        }
    }
}