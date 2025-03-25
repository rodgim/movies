package com.rodgim.movies.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.rodgim.data.source.LocalDataSource
import com.rodgim.movies.FakeLocalDataSource
import com.rodgim.movies.defaultFakeMovies
import com.rodgim.movies.initMockedDi
import com.rodgim.movies.mockedMovie
import com.rodgim.usecases.CheckIfMovieIsFavorite
import com.rodgim.usecases.FindMovieById
import com.rodgim.usecases.ToggleMovieFavorite
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class DetailIntegrationTests : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<DetailViewModel.UiModel>

    private lateinit var vm: DetailViewModel
    private lateinit var localDataSource: FakeLocalDataSource

    @Before
    fun setUp() {
        val vmModule = module {
            factory { (id: Int) -> DetailViewModel(id, get(), get(), get(), get()) }
            factory { FindMovieById(get()) }
            factory { ToggleMovieFavorite(get()) }
            factory { CheckIfMovieIsFavorite(get()) }
        }

        initMockedDi(vmModule)
        vm = get { parametersOf(1) }

        localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.movies = defaultFakeMovies
    }

    @Test
    fun `observing LiveData finds the movie`() {
        vm.model.observeForever(observer)

        verify(observer).onChanged(DetailViewModel.UiModel(mockedMovie.copy(1), false))
    }

    @Test
    fun `favorite is updated in local data source`() {
        vm.model.observeForever(observer)

        vm.onFavoriteClicked()

        runBlocking {
            println("isfavorite=${localDataSource.findMovieById(1).isFavorite}")
            assertTrue(localDataSource.findMovieById(1).isFavorite)
        }
    }
}