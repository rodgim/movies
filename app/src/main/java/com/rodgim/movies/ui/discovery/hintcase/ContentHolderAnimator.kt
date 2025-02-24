package com.rodgim.movies.ui.discovery.hintcase

import android.animation.ValueAnimator
import android.view.View

const val DEFAULT_ANIMATION_DURATION_IN_MILLISECONDS = 300
abstract class ContentHolderAnimator(
    protected var durationInMilliseconds: Int = DEFAULT_ANIMATION_DURATION_IN_MILLISECONDS
) {
    protected var startDelayInMilliseconds: Long = 0

    fun getAnimator(view: View): ValueAnimator = getAnimator(view, NO_CALLBACK)

    abstract fun getAnimator(view: View, onFinishListener: OnFinishListener?): ValueAnimator

    fun setStartDelay(startDelayInMilliseconds: Long): ContentHolderAnimator {
        this.startDelayInMilliseconds = startDelayInMilliseconds
        return this
    }

    interface OnFinishListener {
        fun onFinish()
    }

    companion object {
        val NO_ANIMATOR: ContentHolderAnimator? = null
        val NO_CALLBACK: OnFinishListener? = null
    }
}