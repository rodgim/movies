package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.entities.Movie

class ToggleMovieFavorite(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(movie: Movie): Boolean {
        val isMovieFavorite = moviesRepository.isMovieFavorite(movie.id)
        if (isMovieFavorite) {
            moviesRepository.deleteFavoriteMovie(movie)
        } else {
            moviesRepository.insertFavoriteMovie(movie)
        }
        return !isMovieFavorite
    }
}