package com.rodgim.movies.ui.discovery.hintcase.shapeanimators

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.view.View
import com.rodgim.movies.ui.discovery.hintcase.shapes.RectangularShape
import com.rodgim.movies.ui.discovery.hintcase.shapes.Shape

class RevealRectangularShapeAnimator : ShapeAnimator {
    private var floatEvaluator: FloatEvaluator? = null

    constructor() : super() {
        init()
    }

    constructor(durationInMilliseconds: Int) : super(durationInMilliseconds) {
        init()
    }

    private fun init() {
        floatEvaluator = FloatEvaluator()
    }

    override fun getAnimator(
        view: View, shape: Shape,
        onFinishListener: OnFinishListener?,
    ): ValueAnimator {
        val rectangularShape: RectangularShape = shape as RectangularShape
        val valueAnimator = ValueAnimator.ofFloat(
            rectangularShape.maxHeight,
            rectangularShape.minHeight
        )
        valueAnimator.startDelay = startDelayInMilliseconds
        valueAnimator.setDuration(durationInMilliseconds.toLong())
            .addUpdateListener { valueAnimator ->
                rectangularShape.currentHeight = (valueAnimator.animatedValue as Float)
                val fraction = valueAnimator.animatedFraction
                rectangularShape.currentWidth = (
                    floatEvaluator!!.evaluate(
                        fraction, rectangularShape.maxWidth,
                        rectangularShape.minWidth
                    )
                )
                if (rectangularShape.currentHeight == rectangularShape.minHeight) {
                    rectangularShape.setMinimumValue()
                    if (onFinishListener != null) {
                        onFinishListener.onFinish()
                    }
                }
                view.invalidate()
            }
        return valueAnimator
    }
}