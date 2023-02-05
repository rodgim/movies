package com.rodgim.movies.ui.detail

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.rodgim.movies.ui.common.cancelTransition

class MovieDetailAnimator {

    internal fun postponeEnterTransition(activity: FragmentActivity) = activity.postponeEnterTransition()
    internal fun cancelTransition(view: View) = view.cancelTransition()

    internal fun scaleUpView(view: View) = scaleView(view, SCALE_UP_VALUE, SCALE_UP_VALUE, SCALE_UP_DURATION)
    internal fun scaleDownView(view: View) = scaleView(view, SCALE_DOWN_VALUE, SCALE_DOWN_VALUE, SCALE_DOWN_DURATION)

    internal fun fadeVisible(viewContainer: ViewGroup, view: View) = beginTransitionFor(viewContainer, view, View.VISIBLE)
    internal fun fadeInvisible(viewContainer: ViewGroup, view: View) = beginTransitionFor(viewContainer, view, View.INVISIBLE)

    private fun scaleView(view: View, x: Float, y: Float, duration: Long) =
        view.animate()
            .scaleX(x)
            .scaleY(y)
            .setDuration(duration)
            .setInterpolator(FastOutSlowInInterpolator())
            .withLayer()
            .setListener(null)
            .start()

    private fun beginTransitionFor(viewContainer: ViewGroup, view: View, visibility: Int) {
        val transition = Fade()
        transition.startDelay = TRANSITION_DELAY
        transition.duration = TRANSITION_DURATION
        TransitionManager.beginDelayedTransition(viewContainer, transition)
        view.visibility = visibility
    }

    companion object {
        private const val TRANSITION_DELAY = 100L
        private const val TRANSITION_DURATION = 200L

        private const val SCALE_UP_VALUE = 1.0F
        private const val SCALE_UP_DURATION = 400L

        private const val SCALE_DOWN_VALUE = 0.0F
        private const val SCALE_DOWN_DURATION = 200L
    }
}