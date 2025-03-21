package com.rodgim.movies

import com.rodgim.data.repository.PermissionChecker
import com.rodgim.data.source.LocalDataSource
import com.rodgim.data.source.LocationDataSource
import com.rodgim.data.source.RemoteDataSource
import com.rodgim.entities.Movie
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun initMockedDi(vararg modules: Module) {
    startKoin {
        modules(listOf(mockedAppModule, dataModule) + modules)
    }
}

private val mockedAppModule = module {
    single(named("apiKey")) { "12456"}
    single<LocalDataSource> { FakeLocalDataSource() }
    single<RemoteDataSource> { FakeRemoteDataSource() }
    single<LocationDataSource> { FakeLocationDataSource() }
    single<PermissionChecker> { FakePermissionChecker() }
    single { Dispatchers.Unconfined }
}

val defaultFakeMovies = listOf(
    mockedMovie.copy(1),
    mockedMovie.copy(2),
    mockedMovie.copy(3),
    mockedMovie.copy(4)
)

class FakeLocalDataSource : LocalDataSource {
    var movies: List<Movie> = emptyList()

    override suspend fun isEmpty() = movies.isEmpty()

    override suspend fun isCategoryEmpty(category: String): Boolean {
        return false
    }

    override suspend fun saveMovies(movies: List<Movie>, category: String) {
        this.movies = movies
    }

    override suspend fun getMoviesFromCategory(category: String): List<Movie> {
        return movies
    }

    override suspend fun findMovieById(id: Int): Movie = movies.first { it.id == id }

    override suspend fun updateMovie(movie: Movie) {
        movies = movies.filterNot { it.id == movie.id } + movie
    }

    override suspend fun getFavoriteMovies(): List<Movie> {
        return movies
    }

    override suspend fun toggleFavoriteMovie(movie: Movie) {
        movies = movies.filterNot { it.id == movie.id } + movie.copy(isFavorite = !movie.isFavorite)
    }

    override suspend fun isMovieFavorite(id: Int): Boolean {
        return movies.first { it.id == id }.isFavorite
    }
}

class FakeRemoteDataSource : RemoteDataSource {

    var movies = defaultFakeMovies

    override suspend fun getMoviesFromCategory(
        category: String,
        region: String,
        apikey: String
    ): List<Movie> {
        return movies
    }

    override suspend fun getMovieDetail(movieId: Int, apikey: String): Movie {
        return movies.first{ it.id == movieId}
    }
}

class FakeLocationDataSource : LocationDataSource {
    var location = "US"

    override suspend fun findLastRegion(): String? = location
}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true

    override suspend fun check(permission: PermissionChecker.Permission): Boolean = permissionGranted
}
