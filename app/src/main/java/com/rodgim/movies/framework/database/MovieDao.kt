package com.rodgim.movies.framework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovieDao {

    @Query("SELECT * FROM Movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun findById(id: Int): Movie

    @Query("SELECT COUNT(id) FROM Movie")
    fun movieCount(): Int

    @Query("SELECT * FROM Movie WHERE category = :category")
    fun getMoviesFromCategory(category: String): List<Movie>

    @Query("SELECT COUNT(id) FROM Movie WHERE category = :category")
    fun categoryMoviesCount(category: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: List<Movie>)

    @Update
    fun updateMovie(movie: Movie)
}