package com.rodgim.movies.ui.discovery.hintcase.contentholderanimators

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import com.rodgim.movies.ui.discovery.hintcase.ContentHolderAnimator

class SlideInFromRightContentHolderAnimator : ContentHolderAnimator {
    constructor() : super()

    constructor(durationInMilliseconds: Int) : super(durationInMilliseconds)

    override fun getAnimator(view: View, onFinishListener: OnFinishListener?): ValueAnimator {
        val spaceUntilRightSide = (view.rootView.width - view.left).toFloat()
        val animator = ObjectAnimator.ofFloat(
            view, View.TRANSLATION_X,
            spaceUntilRightSide, 0f
        )
        animator.setDuration(durationInMilliseconds.toLong())
        animator.startDelay = startDelayInMilliseconds
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                view.alpha = 1f
            }

            override fun onAnimationEnd(animation: Animator) {
                if (onFinishListener !== NO_CALLBACK) {
                    onFinishListener?.onFinish()
                }
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        return animator
    }
}