package com.rodgim.movies.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rodgim.entities.Movie
import com.rodgim.movies.ui.common.ScopedViewModel
import com.rodgim.usecases.CheckIfMovieIsFavorite
import com.rodgim.usecases.FindMovieById
import com.rodgim.usecases.ToggleMovieFavorite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class DetailViewModel(
    private val movieId: Int,
    private val findMovieById: FindMovieById,
    private val toggleMovieFavorite: ToggleMovieFavorite,
    private val checkIfMovieIsFavorite: CheckIfMovieIsFavorite,
    override val uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    data class UiModel(val movie: Movie, val isFavorite: Boolean)

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) findMovie()
            return _model
        }

    private fun findMovie() = launch {
        val movie = findMovieById.invoke(movieId)
        val isMovieFavorite = checkIfMovieIsFavorite.invoke(movieId)
        _model.value = UiModel(movie, isMovieFavorite)
    }

    fun onFavoriteClicked() = launch {
        _model.value?.movie?.let {
            _model.value = UiModel(it, toggleMovieFavorite.invoke(it))
        }
    }
}