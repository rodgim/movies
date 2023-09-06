package com.rodgim.movies.framework.database

import com.rodgim.data.source.LocalDataSource
import com.rodgim.entities.Movie
import com.rodgim.movies.framework.toDomainMovie
import com.rodgim.movies.framework.toRoomMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: MovieDatabase) : LocalDataSource {

    private val movieDao = db.movieDao()

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) { movieDao.movieCount() <= 0 }

    override suspend fun isCategoryEmpty(category: String): Boolean =
        withContext(Dispatchers.IO) { movieDao.categoryMoviesCount(category) <= 0 }

    override suspend fun saveMovies(movies: List<Movie>, category: String) {
        withContext(Dispatchers.IO) { movieDao.insertMoviesWithCategories(movies.map { it.toRoomMovie() }, category) }
    }

    override suspend fun getMoviesFromCategory(category: String): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getMoviesFromCategory(category).map { it.toDomainMovie() }
    }

    override suspend fun findMovieById(id: Int): Movie = withContext(Dispatchers.IO) {
        movieDao.findMovieById(id).toDomainMovie()
    }

    override suspend fun updateMovie(movie: Movie) {
        withContext(Dispatchers.IO) { movieDao.upsertMovie(movie.toRoomMovie()) }
    }

    override suspend fun getFavoriteMovies(): List<Movie> = movieDao.getFavoriteMovies().map { it.toDomainMovie() }

    override suspend fun isMovieFavorite(id: Int): Boolean = movieDao.isMovieFavorite(id)

    override suspend fun toggleFavoriteMovie(movie: Movie) = movieDao.upsertMovie(movie.toRoomMovie())
}