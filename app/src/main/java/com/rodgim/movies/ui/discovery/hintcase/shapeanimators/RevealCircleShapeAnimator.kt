package com.rodgim.movies.ui.discovery.hintcase.shapeanimators

import android.animation.ValueAnimator
import android.view.View
import com.rodgim.movies.ui.discovery.hintcase.shapes.CircularShape
import com.rodgim.movies.ui.discovery.hintcase.shapes.Shape

class RevealCircleShapeAnimator : ShapeAnimator {
    constructor() : super()

    constructor(durationInMilliseconds: Int) : super(durationInMilliseconds)

    override fun getAnimator(
        view: View, shape: Shape,
        onFinishListener: OnFinishListener?,
    ): ValueAnimator {
        val circularShape: CircularShape = shape as CircularShape
        val valueAnimator = ValueAnimator.ofFloat(
            circularShape.maxRadius,
            circularShape.minRadius
        )
        valueAnimator.startDelay = startDelayInMilliseconds
        valueAnimator.setDuration(durationInMilliseconds.toLong())
            .addUpdateListener { valueAnimator ->
                circularShape.currentRadius = (valueAnimator.animatedValue as Float)
                if (circularShape.currentRadius == circularShape.minRadius) {
                    if (onFinishListener != null) {
                        onFinishListener.onFinish()
                    }
                }
                view.invalidate()
            }
        return valueAnimator
    }
}