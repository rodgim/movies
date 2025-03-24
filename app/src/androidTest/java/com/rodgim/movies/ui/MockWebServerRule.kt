package com.rodgim.movies.ui

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.concurrent.thread

class MockWebServerRule : TestRule {

    val server = MockWebServer()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when {
                request.path?.startsWith("/movie/popular") == true ->
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(readJsonFile("popular_movies.json"))

                request.path?.startsWith("/movie/top_rated") == true ->
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(readJsonFile("top_rated_movies.json"))

                request.path?.startsWith("/movie/now_playing") == true ->
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(readJsonFile("now_playing.json"))

                request.path?.startsWith("/movie/") == true -> {
                    val basePath = request.path?.substringBefore("?")
                    val movieId = basePath?.split("/")?.last()
                    MockResponse().setBody(readJsonFile("movie_details_$movieId.json"))
                }

                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    private fun readJsonFile(filename: String): String {
        return this::class.java.classLoader
            ?.getResourceAsStream(filename)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalArgumentException("File not found: $filename")
    }

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            server.dispatcher = dispatcher
            server.start()
            replaceBaseUrl()

            try {
                base.evaluate()
            } finally {
                server.shutdown()
            }
        }
    }

    private fun replaceBaseUrl() {
        val testModule = module {
            single(named("baseUrl")) { askMockServerUrlOnAnotherThread() }
        }
        loadKoinModules(testModule)
    }

    private fun askMockServerUrlOnAnotherThread(): String {
        /*
        This needs to be done immediately, but the App will crash with
        "NetworkOnMainThreadException" if this is not extracted from the main thread. So this is
        a "hack" to prevent it. We don't care about blocking in a test, and it's fast.
         */
        var url = ""
        val t = thread {
            url = server.url("/").toString()
        }
        t.join()
        return url
    }
}