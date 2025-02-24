package com.rodgim.movies.ui.discovery.hintcase.shapes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

abstract class Shape {
    var left: Int = 0
    var top: Int = 0
    var right: Int = 0
    var bottom: Int = 0
    var centerX: Float = 0f
    var centerY: Float = 0f
    var width: Float = 0f
    var height: Float = 0f
    var shapePaint: Paint

    init {
        shapePaint = getInitializedTypePaint()
    }

    private fun getInitializedTypePaint(): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(DEFAULT_COLOR)
        paint.alpha = DEFAULT_ALPHA
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        return paint
    }

    abstract fun setMinimumValue()
    abstract fun setShapeInfo(targetView: View, parent: ViewGroup, offset: Int, context: Context)
    abstract fun isTouchEventInsideTheHint(event: MotionEvent): Boolean
    abstract fun draw(canvas: Canvas)

    companion object {
        private const val DEFAULT_COLOR: Int = 0xFFFFFF
        private const val DEFAULT_ALPHA: Int = 0
    }
}