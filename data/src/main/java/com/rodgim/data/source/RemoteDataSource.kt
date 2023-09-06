package com.rodgim.data.source

import com.rodgim.entities.Movie

interface RemoteDataSource {
    suspend fun getMoviesFromCategory(category: String, region: String, apikey: String): List<Movie>
    suspend fun getMovieDetail(movieId: Int, apikey: String): Movie
}