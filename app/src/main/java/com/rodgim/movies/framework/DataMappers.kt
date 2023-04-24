package com.rodgim.movies.framework

import com.rodgim.entities.Movie
import com.rodgim.movies.framework.database.FavoriteMovie as DbFavoriteMovie
import com.rodgim.movies.framework.database.Movie as DbMovie
import com.rodgim.movies.framework.server.Movie as ServerMovie

fun Movie.toRoomMovie(category: String = ""): DbMovie =
    DbMovie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
        category
    )

fun Movie.toRoomFavoriteMovie(): DbFavoriteMovie =
    DbFavoriteMovie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
    )

fun DbMovie.toDomainMovie(): Movie =
    Movie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage
    )

fun ServerMovie.toDomainMovie(): Movie =
    Movie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath ?: posterPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage
    )

fun DbFavoriteMovie.toDomainMovie(): Movie =
    Movie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage
    )
