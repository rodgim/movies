package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.domain.Movie

class ToggleMovieFavorite(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(movie: Movie): Movie = with(movie) {
        copy(favorite = !favorite).also { moviesRepository.updateMovie(it) }
    }
}