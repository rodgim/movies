package com.rodgim.movies.framework.server

import com.rodgim.data.source.RemoteDataSource
import com.rodgim.entities.Movie
import com.rodgim.movies.framework.toDomainMovie

class ServerMovieDataSource(private val retrofit: RetrofitModule) : RemoteDataSource{
    override suspend fun getMoviesFromCategory(category: String, apikey: String): List<Movie> {
        return retrofit.service
            .getMoviesFromCategory(category = category, apiKey = apikey)
            .results
            .map { it.toDomainMovie() }
    }

    override suspend fun getPopularMovies(apikey: String, region: String): List<Movie> {
        return retrofit.service
            .listPopularMovies(apikey, region)
            .results
            .map { it.toDomainMovie() }
    }
}