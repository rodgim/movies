package com.rodgim.movies.ui.discovery.hintcase.utils

import android.graphics.Path

class RoundRect(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    rx: Float,
    ry: Float,
    applyRoundToTopLeft: Boolean = true,
    applyRoundToTopRight: Boolean = true,
    applyRoundToBottomRight: Boolean = true,
    applyRoundToBottomLeft: Boolean = true
) {
    private val path: Path

    init {
        val width = right - left
        val height = bottom - top
        val rx: Float = normalizeValue(rx, 0f, width / 2)
        val ry: Float = normalizeValue(ry, 0f, height / 2)
        val widthMinusCorners = (width - (2 * rx))
        val heightMinusCorners = (height - (2 * ry))
        path = Path()
        path.moveTo(right, top + ry)
        drawTopRightCorner(rx, ry, applyRoundToTopRight)
        path.rLineTo(-widthMinusCorners, 0f)
        drawTopLeftCorner(rx, ry, applyRoundToTopLeft)
        path.rLineTo(0f, heightMinusCorners)
        drawBottomLeftCorner(rx, ry, applyRoundToBottomLeft)
        path.rLineTo(widthMinusCorners, 0f)
        drawBottomRightCorner(rx, ry, applyRoundToBottomRight)
        path.rLineTo(0f, -heightMinusCorners)
        path.close()
    }

    private fun drawBottomRightCorner(rx: Float, ry: Float, applyRoundToBottomRight: Boolean) {
        if (applyRoundToBottomRight) {
            path.rQuadTo(rx, 0f, rx, -ry)
        } else {
            path.rLineTo(rx, 0f)
            path.rLineTo(0f, -ry)
        }
    }

    private fun drawBottomLeftCorner(rx: Float, ry: Float, applyRoundToBottomLeft: Boolean) {
        if (applyRoundToBottomLeft) {
            path.rQuadTo(0f, ry, rx, ry)
        } else {
            path.rLineTo(0f, ry)
            path.rLineTo(rx, 0f)
        }
    }

    private fun drawTopLeftCorner(rx: Float, ry: Float, applyRoundToTopLeft: Boolean) {
        if (applyRoundToTopLeft) {
            path.rQuadTo(-rx, 0f, -rx, ry)
        } else {
            path.rLineTo(-rx, 0f)
            path.rLineTo(0f, ry)
        }
    }

    private fun drawTopRightCorner(rx: Float, ry: Float, applyRoundToTopRight: Boolean) {
        if (applyRoundToTopRight) {
            path.rQuadTo(0f, -ry, -rx, -ry)
        } else {
            path.rLineTo(0f, -ry)
            path.rLineTo(-rx, 0f)
        }
    }

    private fun normalizeValue(value: Float, min: Float, max: Float): Float {
        return if (value < min) {
            0f
        } else if (value > max) {
            max
        } else {
            value
        }
    }

    fun getPath() = path
}
