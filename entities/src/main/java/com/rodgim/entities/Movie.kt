package com.rodgim.entities

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String,
    val backdropPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val popularity: Double,
    val voteAverage: Double,
    val isFavorite: Boolean,
    val duration: Int,
    val genres: List<Genre> = emptyList()
)