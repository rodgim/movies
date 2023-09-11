package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.entities.Movie

class GetFavoriteMovies(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(id: Int): List<Movie> = moviesRepository.getFavoriteMovies()
}