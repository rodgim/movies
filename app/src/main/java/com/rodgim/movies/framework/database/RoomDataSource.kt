package com.rodgim.movies.framework.database

import com.rodgim.data.source.LocalDataSource
import com.rodgim.entities.Movie
import com.rodgim.movies.framework.toDomainMovie
import com.rodgim.movies.framework.toRoomFavoriteMovie
import com.rodgim.movies.framework.toRoomMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: MovieDatabase) : LocalDataSource {

    private val movieDao = db.movieDao()
    private val favoriteMovieDao = db.favoriteMovieDao()

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) { movieDao.movieCount() <= 0 }

    override suspend fun isCategoryEmpty(category: String): Boolean =
        withContext(Dispatchers.IO) { movieDao.categoryMoviesCount(category) <= 0 }

    override suspend fun saveMovies(movies: List<Movie>, category: String) {
        withContext(Dispatchers.IO) { movieDao.insertMovies(movies.map { it.toRoomMovie(category) }) }
    }

    override suspend fun getPopularMovies(): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getAll().map { it.toDomainMovie() }
    }

    override suspend fun getMoviesFromCategory(category: String): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getMoviesFromCategory(category).map { it.toDomainMovie() }
    }

    override suspend fun findMovieById(id: Int): Movie = withContext(Dispatchers.IO) {
        movieDao.findById(id).toDomainMovie()
    }

    override suspend fun updateMovie(movie: Movie) {
        withContext(Dispatchers.IO) { movieDao.updateMovie(movie.toRoomMovie()) }
    }

    override suspend fun getFavoriteMovies(): List<Movie> = favoriteMovieDao.getFavoriteMovies().map { it.toDomainMovie() }

    override suspend fun isMovieFavorite(id: Int): Boolean = favoriteMovieDao.isMovieFavorite(id)

    override suspend fun insertFavoriteMovie(movie: Movie) = favoriteMovieDao.insertFavoriteMovie(movie.toRoomFavoriteMovie())

    override suspend fun deleteFavoriteMovie(movie: Movie) = favoriteMovieDao.deleteFavoriteMovie(movie.toRoomFavoriteMovie())
}