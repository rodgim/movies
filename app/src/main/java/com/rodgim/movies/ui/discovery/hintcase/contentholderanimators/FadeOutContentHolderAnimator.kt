package com.rodgim.movies.ui.discovery.hintcase.contentholderanimators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import com.rodgim.movies.ui.discovery.hintcase.ContentHolderAnimator

class FadeOutContentHolderAnimator : ContentHolderAnimator {
    constructor() : super()

    constructor(durationInMilliseconds: Int) : super(durationInMilliseconds)

    override fun getAnimator(view: View, onFinishListener: OnFinishListener?): ValueAnimator {
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)
        animator.setDuration(durationInMilliseconds.toLong())
        animator.startDelay = startDelayInMilliseconds
        if (onFinishListener !== NO_CALLBACK) {
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    onFinishListener?.onFinish()
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
        return animator
    }
}