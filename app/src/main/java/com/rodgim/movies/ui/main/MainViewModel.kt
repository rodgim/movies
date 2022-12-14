package com.rodgim.movies.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rodgim.domain.Movie
import com.rodgim.movies.ui.common.ScopedViewModel
import com.rodgim.usecases.GetPopularMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(
    private val getPopularMovies: GetPopularMovies,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    init {
        initScope()
    }

    private fun refresh() {
        _model.value = UiModel.RequestLocationPermission
    }

    fun onCoarsePermissionRequested() {
        launch {
            _model.value = UiModel.Loading
            _model.value = UiModel.Content(getPopularMovies.invoke())
        }
    }

    fun onMovieClicked(movie: Movie) {
        _model.value = UiModel.Navigation(movie)
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

    sealed class UiModel {
        object Loading: UiModel()
        data class Content(val movies: List<Movie>): UiModel()
        data class Navigation(val movie: Movie) : UiModel()
        object RequestLocationPermission : UiModel()
    }
}