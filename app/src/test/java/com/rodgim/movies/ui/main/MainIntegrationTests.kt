package com.rodgim.movies.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.rodgim.data.source.LocalDataSource
import com.rodgim.movies.FakeLocalDataSource
import com.rodgim.movies.defaultFakeMovies
import com.rodgim.movies.initMockedDi
import com.rodgim.movies.mockedMovie
import com.rodgim.movies.ui.home.MoviesViewModel
import com.rodgim.usecases.GetMoviesFromCategory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainIntegrationTests : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<MoviesViewModel.UiModel>

    private lateinit var vm: MoviesViewModel

    @Before
    fun setUp() {
        val vmModule = module {
            factory { MoviesViewModel(get(), get()) }
            factory { GetMoviesFromCategory(get()) }
        }

        initMockedDi(vmModule)
        vm = get()
    }

    @Test
    fun `data is loaded from server when local source is empty`() {
        vm.model.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(MoviesViewModel.UiModel.Content(defaultFakeMovies, defaultFakeMovies, defaultFakeMovies))
    }

    @Test
    fun `data is loaded from local source when available`() {
        val fakeLocalMovies = listOf(mockedMovie.copy(10), mockedMovie.copy(11))
        val localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.isCategoryLoadedWithMovies = true
        localDataSource.movies = fakeLocalMovies
        vm.model.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(MoviesViewModel.UiModel.Content(fakeLocalMovies, fakeLocalMovies, fakeLocalMovies))
    }
}