package com.rodgim.movies.framework.server

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/{category}")
    suspend fun getMoviesFromCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): MovieResult

    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun listPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("region") region: String
    ): MovieResult
}