package com.rodgim.data.source

import com.rodgim.entities.Movie

interface RemoteDataSource {
    suspend fun getPopularMovies(apikey: String, region: String): List<Movie>
}