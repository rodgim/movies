package com.rodgim.movies.ui.discovery.hintcase.shapes

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class CircularShape : Shape() {
    var minRadius: Float = DEFAULT_MIN_RADIUS
        private set
    var maxRadius: Float = DEFAULT_MAX_RADIUS
        private set
    var currentRadius: Float = DEFAULT_MAX_RADIUS

    override fun setMinimumValue() {
        currentRadius = minRadius
    }

    override fun setShapeInfo(
        targetView: View,
        parent: ViewGroup,
        offset: Int,
        context: Context,
    ) {
        if (targetView != null) {
            minRadius = ((max(
                targetView.measuredWidth.toDouble(),
                targetView.measuredHeight.toDouble()
            ) / 2) + offset).toFloat()
            maxRadius =
                max(parent!!.height.toDouble(), parent.width.toDouble()).toFloat()
            val targetViewLocationInWindow = IntArray(2)
            targetView.getLocationInWindow(targetViewLocationInWindow)
            centerX = (targetViewLocationInWindow[0] + targetView.width / 2).toFloat()
            centerY = (targetViewLocationInWindow[1] + targetView.height / 2).toFloat()
            left = ((centerX - minRadius) as Int)
            right = ((centerX + minRadius) as Int)
            top = ((centerY - minRadius) as Int)
            bottom = ((centerY + minRadius) as Int)
            width = (minRadius * 2)
            height = (minRadius * 2)
        } else {
            if (parent != null) {
                minRadius = 0f
                maxRadius = parent.height.toFloat()
                centerX = (parent.measuredWidth / 2).toFloat()
                centerY = (parent.measuredHeight / 2).toFloat()
                left = 0
                top = 0
                right = 0
                bottom = 0
            }
        }
        currentRadius = maxRadius
    }

    override fun isTouchEventInsideTheHint(event: MotionEvent): Boolean {
        val xDelta: Float = Math.abs(event.rawX - centerX)
        val yDelta: Float = Math.abs(event.rawY - centerY)
        val distanceFromFocus = sqrt(xDelta.pow(2) + yDelta.pow(2))
        return distanceFromFocus <= minRadius
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, currentRadius, shapePaint)
    }

    companion object {
        private const val DEFAULT_MIN_RADIUS = 50f
        private const val DEFAULT_MAX_RADIUS = 10000f
    }
}