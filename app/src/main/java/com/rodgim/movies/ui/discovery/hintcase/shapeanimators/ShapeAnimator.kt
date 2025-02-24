package com.rodgim.movies.ui.discovery.hintcase.shapeanimators

import android.animation.ValueAnimator
import android.view.View
import com.rodgim.movies.ui.discovery.hintcase.shapes.Shape

abstract class ShapeAnimator(protected var durationInMilliseconds: Int = DEFAULT_ANIMATION_DURATION_IN_MILLISECONDS) {
    protected var startDelayInMilliseconds: Long = 0

    fun getAnimator(view: View, shape: Shape): ValueAnimator {
        return getAnimator(view, shape, NO_CALLBACK)
    }

    fun setStartDelay(startDelayInMilliseconds: Long): ShapeAnimator {
        this.startDelayInMilliseconds = startDelayInMilliseconds
        return this
    }
    abstract fun getAnimator(view: View, shape: Shape, onFinishListener: OnFinishListener?): ValueAnimator

    interface OnFinishListener {
        fun onFinish()
    }

    companion object {
        const val DEFAULT_ANIMATION_DURATION_IN_MILLISECONDS: Int = 300
        val NO_ANIMATOR: ShapeAnimator? = null
        val NO_CALLBACK: OnFinishListener? = null
    }
}