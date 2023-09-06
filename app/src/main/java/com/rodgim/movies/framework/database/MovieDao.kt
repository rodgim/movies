package com.rodgim.movies.framework.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface MovieDao {

    @Upsert
    fun upsertMovies(movies: List<Movie>)

    @Upsert
    fun upsertMovie(movie: Movie)

    @Upsert
    fun upsertMoviesList(moviesList: List<MovieList>)

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun findMovieById(id: Int): Movie

    @Query("SELECT COUNT(id) FROM Movie")
    fun movieCount(): Int

    @Query("SELECT isFavorite FROM Movie WHERE id=:id")
    suspend fun isMovieFavorite(id: Int): Boolean

    @Query("SELECT * FROM Movie WHERE isFavorite == 1")
    fun getFavoriteMovies(): List<Movie>

    @Query(
        """
            SELECT Movie.*
            FROM Movie
            INNER JOIN movie_list ON Movie.id == movie_list.movie_id
            WHERE movie_list.category_id = :categoryId
        """
    )
    fun getMoviesFromCategory(categoryId: String): List<Movie>

    @Query(
        """
            SELECT COUNT(Movie.id)
            FROM Movie
            INNER JOIN movie_list ON Movie.id == movie_list.movie_id
            WHERE movie_list.category_id = :categoryId
        """
    )
    fun categoryMoviesCount(categoryId: String): Int

    @Query("DELETE FROM movie_list WHERE category_id = :categoryId")
    fun deleteMoviesFromCategory(categoryId: String)

    @Transaction
    fun insertMoviesWithCategories(
        movies: List<Movie>,
        category: String
    ) {
        val categoryList = movies.map { movie ->
            MovieList(category, movie.id)
        }
        upsertMovies(movies)
        upsertMoviesList(categoryList)
    }

    @Transaction
    fun deleteMoviesWithCategories(category: String) {
        deleteMoviesFromCategory(category)
    }
}