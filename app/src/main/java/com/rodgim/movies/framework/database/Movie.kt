package com.rodgim.movies.framework.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodgim.entities.Genre

@Entity
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String,
    val backdropPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val popularity: Double,
    val voteAverage: Double,
    val isFavorite: Boolean = false,
    val genres: List<Genre>? = null,
    val duration: Int? = null
)
