package com.rodgim.movies.framework.server

import com.rodgim.data.source.RemoteDataSource
import com.rodgim.entities.Movie
import com.rodgim.movies.framework.toDomainMovie

class ServerMovieDataSource(private val retrofit: RetrofitModule) : RemoteDataSource{
    override suspend fun getMoviesFromCategory(category: String, region: String, apikey: String): List<Movie> {
        return retrofit.service
            .getMoviesFromCategory(category = category, region = region, apiKey = apikey)
            .results
            .map { it.toDomainMovie() }
    }

    override suspend fun getMovieDetail(movieId: Int, apikey: String): Movie {
        return retrofit.service
            .getMovieDetail(movieId.toString(), apikey)
            .toDomainMovie()
    }
}