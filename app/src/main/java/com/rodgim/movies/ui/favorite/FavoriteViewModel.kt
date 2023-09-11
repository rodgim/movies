package com.rodgim.movies.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rodgim.entities.Movie
import com.rodgim.movies.ui.common.ScopedViewModel
import com.rodgim.usecases.GetFavoriteMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val getFavoriteMovies: GetFavoriteMovies,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) getData()
            return _model
        }

    init {
        initScope()
    }

    private fun getData() {
        launch {
            _model.value = UiModel.Loading
            val result = getFavoriteMovies()
            _model.value = UiModel.Content(result)
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
        data class Content(val favorites: List<Movie>): UiModel()
        data class Navigation(val movie: Movie) : UiModel()
    }
}