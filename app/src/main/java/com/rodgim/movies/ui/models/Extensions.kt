package com.rodgim.movies.ui.models

import com.rodgim.entities.Movie

private const val PLACEHOLDER = "--"

fun Movie.getFullPosterPath(width: Int = 185): String = "https://image.tmdb.org/t/p/w$width/${this.posterPath}"

fun Movie.getFullBackdropPath(width: Int = 780): String = "https://image.tmdb.org/t/p/w$width${this.backdropPath}"

fun Movie.get5StarRating(): Float = this.voteAverage.div(2).toFloat()

fun Movie.getDurationDisplay(): String {
    return if (duration > 0) {
        val hours = this.duration.div(60)
        val minutes = this.duration % 60
        String.format("%d:%02d", hours, minutes)
    } else {
        PLACEHOLDER
    }
}

fun Movie.getGenresDisplay(): String {
    return if (genres.isNotEmpty()) {
        genres.joinToString(", ") { it.name }
    } else {
        PLACEHOLDER
    }
}
