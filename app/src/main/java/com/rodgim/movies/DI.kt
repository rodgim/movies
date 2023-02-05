package com.rodgim.movies

import android.app.Application
import com.rodgim.data.repository.MoviesRepository
import com.rodgim.data.repository.PermissionChecker
import com.rodgim.data.repository.RegionRepository
import com.rodgim.data.source.LocalDataSource
import com.rodgim.data.source.LocationDataSource
import com.rodgim.data.source.RemoteDataSource
import com.rodgim.movies.framework.AndroidPermissionChecker
import com.rodgim.movies.framework.PlayServicesLocationDataSource
import com.rodgim.movies.framework.database.MovieDatabase
import com.rodgim.movies.framework.database.RoomDataSource
import com.rodgim.movies.framework.server.RetrofitModule
import com.rodgim.movies.framework.server.ServerMovieDataSource
import com.rodgim.movies.ui.detail.DetailActivity
import com.rodgim.movies.ui.detail.DetailViewModel
import com.rodgim.movies.ui.home.MoviesFragment
import com.rodgim.movies.ui.home.MoviesViewModel
import com.rodgim.usecases.FindMovieById
import com.rodgim.usecases.GetPopularMovies
import com.rodgim.usecases.ToggleMovieFavorite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(
            appModule,
            dataModule,
            scopesModule
        ))
    }
}

private val appModule = module {
    single(named("apiKey")) { BuildConfig.TMDB_API_KEY}
    single{ MovieDatabase.build(get())}
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { ServerMovieDataSource(get()) }
    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<PermissionChecker> { AndroidPermissionChecker(get()) }
    single<CoroutineDispatcher> { Dispatchers.Main }
    single(named("baseUrl")) { BuildConfig.TMDB_BASE_URL }
    single { RetrofitModule(get(named("baseUrl"))) }
}

val dataModule = module {
    factory { RegionRepository(get(), get()) }
    factory { MoviesRepository(get(), get(), get(), get(named("apiKey"))) }
}

private val scopesModule = module {
    scope(named<DetailActivity>()) {
        viewModel{ (id: Int) -> DetailViewModel(id, get(), get(), get()) }
        scoped { FindMovieById(get()) }
        scoped { ToggleMovieFavorite(get()) }
    }
    scope(named<MoviesFragment>()) {
        viewModel { MoviesViewModel(get(), get()) }
        scoped { GetPopularMovies(get()) }
    }
}