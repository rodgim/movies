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
    val voteAverage: Double
) {

    fun getFullPosterPath(width: Int = 185): String = "https://image.tmdb.org/t/p/w$width/${this.posterPath}"

    fun getFullBackdropPath(width: Int = 780): String = "https://image.tmdb.org/t/p/w$width${this.backdropPath}"
}