package com.rodgim.movies.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rodgim.movies.mockedMovie
import com.rodgim.usecases.CheckIfMovieIsFavorite
import com.rodgim.usecases.FindMovieById
import com.rodgim.usecases.ToggleMovieFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var findMovieById: FindMovieById

    @Mock
    lateinit var toggleMovieFavorite: ToggleMovieFavorite

    @Mock
    lateinit var observer: Observer<DetailViewModel.UiModel>

    @Mock
    lateinit var checkIfMovieIsFavorite: CheckIfMovieIsFavorite

    private lateinit var vm: DetailViewModel

    @Before
    fun setUp() {
        vm = DetailViewModel(1, findMovieById, toggleMovieFavorite, checkIfMovieIsFavorite, Dispatchers.Unconfined)
    }

    @Test
    fun `observing LiveData finds the movie`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(findMovieById.invoke(1)).thenReturn(movie)
            whenever(checkIfMovieIsFavorite.invoke(1)).thenReturn(false)

            vm.model.observeForever(observer)

            verify(observer).onChanged(DetailViewModel.UiModel(movie, false))
        }
    }

    @Test
    fun `when favorite clicked, the toggleMovieFavorite use case is invoked`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(findMovieById.invoke(1)).thenReturn(movie)
            whenever(checkIfMovieIsFavorite.invoke(1)).thenReturn(false)
            whenever(toggleMovieFavorite.invoke(movie)).thenReturn(!movie.isFavorite)
            vm.model.observeForever(observer)

            vm.onFavoriteClicked()

            verify(toggleMovieFavorite).invoke(movie)
        }
    }
}