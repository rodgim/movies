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
import com.rodgim.movies.ui.main.MainActivity
import com.rodgim.movies.ui.main.MainViewModel
import com.rodgim.usecases.GetPopularMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
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
    single(named("apiKey")) { BuildConfig.API_KEY}
    single{ MovieDatabase.build(get())}
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { ServerMovieDataSource(get()) }
    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<PermissionChecker> { AndroidPermissionChecker(get()) }
    single<CoroutineDispatcher> { Dispatchers.Main }
    single(named("baseUrl")) { BuildConfig.BASE_URL }
    single { RetrofitModule(get(named("baseUrl"))) }
}

val dataModule = module {
    factory { RegionRepository(get(), get()) }
    factory { MoviesRepository(get(), get(), get(), get(named("apiKey"))) }
}

private val scopesModule = module {
    scope(named<MainActivity>()) {
        viewModel{ MainViewModel(get(), get())}
        scoped { GetPopularMovies(get()) }
    }
}