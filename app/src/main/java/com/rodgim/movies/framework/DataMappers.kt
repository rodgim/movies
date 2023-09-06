package com.rodgim.movies.framework

import com.rodgim.entities.Genre
import com.rodgim.entities.Movie
import com.rodgim.movies.framework.database.Movie as DbMovie
import com.rodgim.movies.framework.server.Genre as ServerGenre
import com.rodgim.movies.framework.server.Movie as ServerMovie

fun Movie.toRoomMovie(): DbMovie =
    DbMovie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = posterPath,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        popularity = popularity,
        voteAverage = voteAverage,
        isFavorite = isFavorite,
        genres = genres,
        duration = duration
    )

fun DbMovie.toDomainMovie(): Movie =
    Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = posterPath,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        popularity = popularity,
        voteAverage = voteAverage,
        isFavorite = isFavorite,
        genres = genres ?: emptyList(),
        duration = duration ?: 0
    )

fun ServerMovie.toDomainMovie(): Movie =
    Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = posterPath,
        backdropPath = backdropPath ?: posterPath,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        popularity = popularity,
        voteAverage = voteAverage,
        isFavorite = false,
        genres = genres?.map { it.toDomainGenre() } ?: emptyList(),
        duration = runtime ?: 0
    )

fun ServerGenre.toDomainGenre(): Genre =
    Genre(
        id = id,
        name = name
    )
