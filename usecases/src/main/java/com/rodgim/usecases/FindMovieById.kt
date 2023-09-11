package com.rodgim.usecases

import com.rodgim.data.repository.MoviesRepository
import com.rodgim.entities.Movie

class FindMovieById(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(id: Int): Movie = moviesRepository.findMovieById(id)
}