package com.rodgim.movies.framework.server

import com.rodgim.data.source.RemoteDataSource
import com.rodgim.domain.Movie
import com.rodgim.movies.framework.toDomainMovie

class ServerMovieDataSource(private val retrofit: RetrofitModule) : RemoteDataSource{
    override suspend fun getPopularMovies(apikey: String, region: String): List<Movie> {
        return retrofit.service
            .listPopularMovies(apikey, region)
            .results
            .map { it.toDomainMovie() }
    }
}