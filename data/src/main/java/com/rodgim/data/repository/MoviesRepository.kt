package com.rodgim.data.repository

import com.rodgim.data.source.LocalDataSource
import com.rodgim.data.source.RemoteDataSource
import com.rodgim.entities.Movie

class MoviesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val regionRepository: RegionRepository,
    private val apiKey: String
) {

    suspend fun getMoviesFromCategory(category: String): List<Movie> {
        if (localDataSource.isCategoryEmpty(category)) {
            val movies = remoteDataSource.getMoviesFromCategory(category, regionRepository.findLastRegion(), apiKey)
            localDataSource.saveMovies(movies, category)
        }
        return localDataSource.getMoviesFromCategory(category)
    }

    suspend fun findMovieById(id: Int): Movie {
        val movie = remoteDataSource.getMovieDetail(id, apiKey)
        val isMovieFavorite = isMovieFavorite(id)
        localDataSource.updateMovie(movie.copy(isFavorite = isMovieFavorite))
        return localDataSource.findMovieById(id)
    }

    suspend fun updateMovie(movie: Movie) = localDataSource.updateMovie(movie)

    suspend fun getFavoriteMovies() = localDataSource.getFavoriteMovies()

    suspend fun toggleFavoriteMovie(movie: Movie) = localDataSource.toggleFavoriteMovie(movie)

    suspend fun isMovieFavorite(id: Int) = localDataSource.isMovieFavorite(id)
}