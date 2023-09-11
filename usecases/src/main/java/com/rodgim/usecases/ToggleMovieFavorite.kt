package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.entities.Movie

class ToggleMovieFavorite(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(movie: Movie): Boolean {
        val isMovieFavorite = moviesRepository.isMovieFavorite(movie.id)
        moviesRepository.toggleFavoriteMovie(movie.copy(isFavorite = !isMovieFavorite))
        return !isMovieFavorite
    }
}