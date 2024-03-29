package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository

class CheckIfMovieIsFavorite(private val repository: MoviesRepository) {

    suspend operator fun invoke(movieId: Int) = repository.isMovieFavorite(movieId)

}
