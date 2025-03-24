package com.rodgim.movies.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.GrantPermissionRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.rodgim.movies.R
import com.rodgim.movies.framework.server.RetrofitModule
import com.rodgim.movies.ui.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.koin.test.KoinTest
import org.koin.test.get

class UiTest : KoinTest {

    @get:Rule
    private val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(mockWebServerRule)
        .around(
            GrantPermissionRule.grant(
                "android.permission.ACCESS_COARSE_LOCATION"
            )
        )

    private lateinit var resource: IdlingResource

    @Before
    fun setUp() {
        resource = OkHttp3IdlingResource.create("OkHttp", get<RetrofitModule>().okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun clickAMovieNavigatesToDetail() {
        ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(500)

        onView(withId(R.id.rvPopular)).check(matches(hasMinimumChildCount(3)))

        onView(withId(R.id.rvPopular)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.movieDetailToolbar))
            .check(matches(hasDescendant(withText("Snow White"))))

    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(resource)
    }
}