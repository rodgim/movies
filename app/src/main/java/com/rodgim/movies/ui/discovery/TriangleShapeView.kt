package com.rodgim.movies.ui.discovery

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.View

class TriangleShapeView(context: Context): View(context) {
    companion object{
        private val DEFAULT_TRIANGLE_DIRECTION: Direction = Direction.UP
    }

    enum class Direction {
        UP, DOWN, RIGHT, LEFT
    }

    private var pathBackground: Path = Path()
    private var pathLines: Path = Path()
    private var paintBackground: Paint = Paint()
    private var paintLines: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var borderColor: Int? = null
    private var backgroundColor: Int? = null
    private var borderWidth: Int? = null
    private var shadowSize: Int? = null
    private var useBorder: Boolean = false
    private var direction: Direction

    init {
        direction = DEFAULT_TRIANGLE_DIRECTION
        paintLines.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        drawBackground(canvas, width, height)
        if (useBorder){
            drawBorder(canvas, width, height)
        }
        if (direction != Direction.UP){
            rotateView(direction)
        }
    }

    private fun drawBorder(canvas: Canvas?, width: Float, height: Float){
        borderColor?.let { bc ->
            paintLines.color = bc
        }
        borderWidth?.let { bw ->
            paintLines.strokeWidth = bw.toFloat()
        }
        pathLines.reset()
        pathLines.moveTo(0f, height)
        pathLines.lineTo(width / 2, height / 3)
        pathLines.lineTo(width, height)
        pathLines.lineTo(width / 2, height / 3)
        pathLines.lineTo(0f, height)
        pathLines.close()
        canvas?.drawPath(pathLines, paintLines)
    }

    private fun drawBackground(canvas: Canvas?, width: Float, height: Float){
        backgroundColor?.let { bc ->
            paintBackground.color = bc
        }
        pathBackground.reset()
        pathBackground.moveTo(0f, height)
        pathBackground.lineTo(width / 2, height / 3)
        pathBackground.lineTo(width, height)
        pathBackground.close()
        shadowSize?.let { ss ->
            paintBackground.setShadowLayer(ss.toFloat(), 1f, 1f, Color.BLACK)
        }
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        canvas?.drawPath(pathBackground, paintBackground)
    }

    private fun rotateView(direction: Direction){
        when(direction){
            Direction.RIGHT -> this.rotation = 90f
            Direction.DOWN -> this.rotation = 180f
            Direction.LEFT -> this.rotation = 270f
            Direction.UP -> this.rotation = 0f
        }
    }

    fun setTriangleBackgroundColor(color: Int){
        this.backgroundColor = color
    }

    fun setBorder(width: Int, color: Int){
        this.useBorder = true
        this.borderWidth = width
        this.borderColor = color
    }

    fun setDirection(direction: Direction){
        this.direction = direction
    }

    fun setShadowSize(shadowSize: Int){
        this.shadowSize = shadowSize
    }
}