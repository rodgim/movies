package com.rodgim.data.source

import com.rodgim.entities.Movie

interface LocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun isCategoryEmpty(category: String): Boolean
    suspend fun saveMovies(movies: List<Movie>, category: String = "")
    suspend fun getMoviesFromCategory(category: String): List<Movie>
    suspend fun findMovieById(id: Int): Movie
    suspend fun updateMovie(movie: Movie)
    suspend fun getFavoriteMovies(): List<Movie>
    suspend fun toggleFavoriteMovie(movie: Movie)
    suspend fun isMovieFavorite(id: Int): Boolean
}