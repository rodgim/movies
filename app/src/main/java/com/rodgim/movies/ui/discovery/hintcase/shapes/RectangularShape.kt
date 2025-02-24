package com.rodgim.movies.ui.discovery.hintcase.shapes

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.rodgim.movies.ui.discovery.hintcase.utils.RoundRect

class RectangularShape : Shape() {
    var maxHeight = DEFAULT_MAX_HEIGHT
    var minHeight = DEFAULT_MIN_HEIGHT
    var minWidth = DEFAULT_MIN_HEIGHT
    var maxWidth = DEFAULT_MIN_HEIGHT
    var currentHeight = DEFAULT_MAX_HEIGHT
    var currentWidth = DEFAULT_MAX_HEIGHT

    override fun setMinimumValue() {
        currentWidth = minWidth
        currentHeight = minHeight
    }

    override fun setShapeInfo(targetView: View, parent: ViewGroup, offset: Int, context: Context) {
        if (targetView != null) {
            minHeight = targetView.measuredHeight.toFloat() + (offset * 2)
            minWidth = targetView.measuredWidth.toFloat() + (offset * 2)
            maxHeight = (parent.height * 2).toFloat()
            maxWidth = (parent.width * 2).toFloat()
            val targetViewLocationInWindow = IntArray(2)
            targetView.getLocationInWindow(targetViewLocationInWindow)
            centerX = (targetViewLocationInWindow[0] + targetView.width / 2).toFloat()
            centerY = (targetViewLocationInWindow[1] + targetView.height / 2).toFloat()
            left = targetViewLocationInWindow[0] - offset
            right = (targetViewLocationInWindow[0] + minWidth - offset).toInt()
            top = targetViewLocationInWindow[1] - offset
            bottom = (targetViewLocationInWindow[1] + minHeight - offset).toInt()
            width = minWidth
            height = minHeight
        } else {
            if (parent != null) {
                minHeight = 0f
                minWidth = 0f
                maxHeight = parent.height.toFloat()
                maxWidth = parent.width.toFloat()
                centerX = (parent.measuredWidth / 2).toFloat()
                centerY = (parent.measuredHeight / 2).toFloat()
                left = 0
                right = 0
                top = 0
                bottom = 0
            }
        }
        currentHeight = maxHeight
        currentWidth = maxWidth
    }

    override fun isTouchEventInsideTheHint(event: MotionEvent): Boolean {
        return event.rawX <= right
                && event.rawX >= left
                && event.rawY <= bottom
                && event.rawY >= top
    }

    override fun draw(canvas: Canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawRoundRectAfterLollipop(canvas)
        } else {
            drawRoundRectPreLollipop(canvas)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawRoundRectAfterLollipop(canvas: Canvas) {
        canvas.drawRoundRect(
            centerX - (currentWidth / 2),
            centerY - (currentHeight / 2),
            centerX + (currentWidth / 2),
            centerY + (currentHeight / 2),
            10f,
            10f,
            shapePaint
        )
    }

    private fun drawRoundRectPreLollipop(canvas: Canvas) {
        val roundRect = RoundRect(
            centerX - (currentWidth / 2),
            centerY - (currentHeight / 2),
            centerX + (currentWidth / 2),
            centerY + (currentHeight / 2),
            10f,
            10f
        )
        canvas.drawPath(roundRect.getPath(), shapePaint)
    }

    companion object {
        private const val DEFAULT_MIN_HEIGHT: Float = 50f
        private const val DEFAULT_MAX_HEIGHT: Float = 10000f
    }
}