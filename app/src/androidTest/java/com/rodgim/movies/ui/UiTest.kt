package com.rodgim.movies.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.rodgim.movies.R
import com.rodgim.movies.framework.server.RetrofitModule
import com.rodgim.movies.ui.main.MainActivity
import com.rodgim.movies.utils.fromJson
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.koin.test.KoinTest
import org.koin.test.get

class UiTest : KoinTest {

    private val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(mockWebServerRule)
        .around(
            GrantPermissionRule.grant(
                "android.permission.ACCESS_COARSE_LOCATION"
            )
        )
        .around(ActivityScenarioRule(MainActivity::class.java))

    @Before
    fun setUp() {
        mockWebServerRule.server.enqueue(
            MockResponse().fromJson("popularmovies.json")
        )

        val resource = OkHttp3IdlingResource.create("OkHttp", get<RetrofitModule>().okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun clickAMovieNavigatesToDetail() {
        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                click()
            )
        )
        onView(withId(R.id.movieDetailToolbar))
            .check(matches(hasDescendant(withText("Black Adam"))))
    }
}