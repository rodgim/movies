package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.entities.Movie

class GetMoviesFromCategory(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(category: String): List<Movie> = moviesRepository.getMoviesFromCategory(category)
}