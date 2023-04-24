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
            val movies = remoteDataSource.getMoviesFromCategory(category, apiKey)
            localDataSource.saveMovies(movies, category)
        }
        return localDataSource.getMoviesFromCategory(category)
    }

    suspend fun getPopularMovies(): List<Movie> {
        if (localDataSource.isEmpty()) {
            val movies = remoteDataSource.getPopularMovies(apiKey, regionRepository.findLastRegion())
            localDataSource.saveMovies(movies)
        }
        return localDataSource.getPopularMovies()
    }

    suspend fun findMovieById(id: Int): Movie = localDataSource.findMovieById(id)

    suspend fun updateMovie(movie: Movie) = localDataSource.updateMovie(movie)

    suspend fun getFavoriteMovies() = localDataSource.getFavoriteMovies()

    suspend fun insertFavoriteMovie(movie: Movie) = localDataSource.insertFavoriteMovie(movie)

    suspend fun deleteFavoriteMovie(movie: Movie) = localDataSource.deleteFavoriteMovie(movie)

    suspend fun isMovieFavorite(id: Int) = localDataSource.isMovieFavorite(id)
}