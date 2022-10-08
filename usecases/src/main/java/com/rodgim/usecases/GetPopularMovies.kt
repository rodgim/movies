package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.domain.Movie

class GetPopularMovies(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(): List<Movie> = moviesRepository.getPopularMovies()
}