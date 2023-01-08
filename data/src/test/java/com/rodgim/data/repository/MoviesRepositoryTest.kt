package com.rodgim.data.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rodgim.data.mockedMovie
import com.rodgim.data.source.LocalDataSource
import com.rodgim.data.source.LocationDataSource
import com.rodgim.data.source.RemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MoviesRepositoryTest {

    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var regionRepository: RegionRepository

    lateinit var moviesRepository: MoviesRepository

    private val apiKey = "1a2b3c4d"

    @Before
    fun setUp() {
        moviesRepository = MoviesRepository(localDataSource, remoteDataSource, regionRepository, apiKey)
    }

    @Test
    fun `getPopularMovies gets from local data source first`() {
        runBlocking {
            val localMovies = listOf(mockedMovie.copy(id = 1))
            whenever(localDataSource.isEmpty()).thenReturn(false)
            whenever(localDataSource.getPopularMovies()).thenReturn(localMovies)

            val result = moviesRepository.getPopularMovies()
            assertEquals(localMovies, result)
        }
    }

    @Test
    fun `getPopularMovies saves remote data to local`() {
        runBlocking {
            val remoteMovies = listOf(mockedMovie.copy(id = 2))
            whenever(localDataSource.isEmpty()).thenReturn(true)
            whenever(remoteDataSource.getPopularMovies(any(), any())).thenReturn(remoteMovies)
            whenever(regionRepository.findLastRegion()).thenReturn("US")

            moviesRepository.getPopularMovies()
            verify(localDataSource).saveMovies(remoteMovies)
        }
    }

    @Test
    fun `findById calls local data source`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 5)
            whenever(localDataSource.findMovieById(5)).thenReturn(movie)

            val result = moviesRepository.findMovieById(5)

            assertEquals(movie, result)
        }
    }

    @Test
    fun `update updates local data source`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            moviesRepository.updateMovie(movie)
            verify(localDataSource).updateMovie(movie)
        }
    }
}