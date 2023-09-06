package com.rodgim.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rodgim.entities.Movie
import com.rodgim.movies.ui.common.ScopedViewModel
import com.rodgim.movies.ui.models.CategoryMovie
import com.rodgim.usecases.GetMoviesFromCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getMoviesFromCategory: GetMoviesFromCategory,
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
            val popular = async { getMoviesFromCategory.invoke(CategoryMovie.POPULAR.id) }
            val nowPlaying = async { getMoviesFromCategory.invoke(CategoryMovie.NOW_PLAYING.id) }
            val topRated = async { getMoviesFromCategory.invoke(CategoryMovie.TOP_RATED.id) }

            val defPopular = popular.await()
            val defNowPlaying = nowPlaying.await()
            val defTopRated = topRated.await()

            _model.value = UiModel.Content(popular = defPopular, nowPlaying = defNowPlaying, topRated = defTopRated)
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
        data class Content(val popular: List<Movie>, val nowPlaying: List<Movie>, val topRated: List<Movie>): UiModel()
        data class Navigation(val movie: Movie) : UiModel()
        object RequestLocationPermission : UiModel()
    }
}