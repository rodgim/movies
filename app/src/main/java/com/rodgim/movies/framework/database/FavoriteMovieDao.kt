package com.rodgim.movies.framework.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movie")
    fun getFavoriteMovies(): List<FavoriteMovie>

    @Query("SELECT EXISTS(SELECT * FROM favorite_movie WHERE id=:id)")
    suspend fun isMovieFavorite(id: Int): Boolean

    @Insert
    suspend fun insertFavoriteMovie(movie: FavoriteMovie)

    @Delete
    suspend fun deleteFavoriteMovie(movie: FavoriteMovie)
}