package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.entities.Movie

class GetFavoriteMovies(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(): List<Movie> = moviesRepository.getFavoriteMovies()
}