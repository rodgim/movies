package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.domain.Movie

class FindMovieById(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(id: Int): Movie = moviesRepository.findMovieById(id)
}