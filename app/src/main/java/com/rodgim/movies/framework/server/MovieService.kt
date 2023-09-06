package com.rodgim.movies.framework.server

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/{category}")
    suspend fun getMoviesFromCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 1,
        @Query("region") region: String,
        @Query("api_key") apiKey: String
    ): MovieResult

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: String,
        @Query("api_key") apiKey: String
    ): Movie
}