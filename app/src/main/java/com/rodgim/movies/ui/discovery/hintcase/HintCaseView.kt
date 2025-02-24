package com.rodgim.movies.ui.discovery.hintcase

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.rodgim.movies.ui.discovery.hintcase.shapeanimators.ShapeAnimator
import com.rodgim.movies.ui.discovery.hintcase.shapes.RectangularShape
import com.rodgim.movies.ui.discovery.hintcase.shapes.Shape
import com.rodgim.movies.ui.discovery.hintcase.utils.DimenUtils

class HintCaseView(val activity: Activity, var hintCase: HintCase?): RelativeLayout(activity) {
    private val DEFAULT_SHAPE: Shape = RectangularShape()
    private var NO_BLOCK_INFO: ContentHolder? = null
    private var NO_BLOCK_INFO_VIEW: View? = null
    private var NO_TARGET_VIEW: View? = null

    private var targetView: View? = null
    private var bitmap: Bitmap? = null
    private var shape: Shape = DEFAULT_SHAPE
    private var hintBlock: ContentHolder? = null
    private var basePaint: Paint
    private var hintBlockView: View?
    private var parent: ViewGroup? = null
    private var parentIndex: Int = -1
    private var backgroundColor: Int
    private var offsetInPx: Int
    private var hintBlockPosition: Int = DEFAULT_HINT_BLOCK_POSITION
    private var onClosedListener: HintCase.OnClosedListener? = null
    private var showShapeAnimator: ShapeAnimator? = null
    private var hideShapeAnimator: ShapeAnimator? = null
    private var showContentHolderAnimator: ContentHolderAnimator? = null
    private var hideContentHolderAnimator: ContentHolderAnimator? = null
    private var extraBlocks: MutableList<ContentHolder> = mutableListOf()
    private var extraBlockViews: MutableList<View> = mutableListOf()
    private var showExtraContentHolderAnimators: MutableList<ContentHolderAnimator?> = mutableListOf()
    private var hideExtraContentHolderAnimators: MutableList<ContentHolderAnimator?> = mutableListOf()
    private var closeOnTouch: Boolean
    private var isTargetClickable: Boolean = false
    private var navigationBarSizeIfExistAtTheBottom: Point
    private var navigationBarSizeIfExistAtTheRight: Point
    private var wasPressedOnShape: Boolean = false

    fun getHintBlockView(): View? = hintBlockView

    init {
        setLayoutParams(
            LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        closeOnTouch = true
        hintBlock = NO_BLOCK_INFO
        hintBlockView = NO_BLOCK_INFO_VIEW
        backgroundColor = DEFAULT_BACKGROUND_COLOR
        offsetInPx = HintCase.NO_OFFSET_IN_PX.toInt()
        hintBlockPosition = DEFAULT_HINT_BLOCK_POSITION
        basePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        navigationBarSizeIfExistAtTheBottom = DimenUtils.getNavigationBarSizeIfExistAtTheBottom(context)
        navigationBarSizeIfExistAtTheRight = DimenUtils.getNavigationBarSizeIfExistOnTheRight(context)
    }

    private fun buildBaseBitmap() {
        bitmap?.recycle()

        if ((parent?.measuredWidth ?: 0) > 0 && (parent?.measuredHeight ?: 0) > 0) {
            bitmap = Bitmap.createBitmap(
                parent?.measuredWidth ?: 0,
                parent?.measuredHeight ?: 0,
                Bitmap.Config.ARGB_8888)
        }
    }

    private fun performShow() {
        parent?.addView(this, parentIndex)
        if (showShapeAnimator != ShapeAnimator.NO_ANIMATOR) {
            val animator: ValueAnimator? = showShapeAnimator?.getAnimator(this, shape, object : ShapeAnimator.OnFinishListener {
                override fun onFinish() {
                    performShowBlocks()
                }
            })
            animator?.start()
        } else {
            shape.setMinimumValue()
            performShowBlocks()
        }
    }

    private fun performShowBlocks() {
        val animators: MutableList<Animator> = mutableListOf()
        if (showContentHolderAnimator != ContentHolderAnimator.NO_ANIMATOR) {
            showContentHolderAnimator?.let {
                hintBlockView?.let { view ->
                    animators.add(it.getAnimator(view))
                }
            }
            //animators.add(showContentHolderAnimator?.getAnimator(hintBlockView))
        }
        if (showExtraContentHolderAnimators.isNotEmpty()) {
            for (i in extraBlocks.indices) {
                val animator = showExtraContentHolderAnimators[i]
                if (animator != ContentHolderAnimator.NO_ANIMATOR) {
                    animators.add(animator!!.getAnimator(extraBlockViews[i]))
                }
            }
        }
        if (animators.isEmpty()) {
            if (existHintBlock()) {
                getHintBlockView()?.alpha = 1f
            }
            for (view in extraBlockViews) {
                view.alpha = 1f
            }
        } else {
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animators)
            animatorSet.addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) = Unit

                override fun onAnimationEnd(animation: Animator) {
                    if (existHintBlock()
                        && showContentHolderAnimator == ContentHolderAnimator.NO_ANIMATOR) {
                        getHintBlockView()?.alpha = 1f
                    }
                    for (i in showExtraContentHolderAnimators.indices) {
                        val animator = showExtraContentHolderAnimators[i]
                        if (animator == ContentHolderAnimator.NO_ANIMATOR) {
                            extraBlockViews[i].alpha = 1f
                        }
                    }
                }

                override fun onAnimationCancel(animation: Animator) = Unit

                override fun onAnimationRepeat(animation: Animator) = Unit

            })
            animatorSet.start()
        }
    }

    fun performHide() {
        val animators: MutableList<Animator> = mutableListOf()
        if (hideContentHolderAnimator != ContentHolderAnimator.NO_ANIMATOR) {
            hideContentHolderAnimator?.let {
                hintBlockView?.let { view: View ->
                    animators.add(it.getAnimator(view))
                }
            }
            //animators.add(hideContentHolderAnimator?.getAnimator(hintBlockView))
        } else {
            if (existHintBlock()) {
                getHintBlockView()?.alpha = 0f
            }
        }
        if (hideExtraContentHolderAnimators.isNotEmpty()) {
            for (i in extraBlocks.indices) {
                val animator = hideExtraContentHolderAnimators[i]
                if (animator != ContentHolderAnimator.NO_ANIMATOR) {
                    animators.add(animator!!.getAnimator(extraBlockViews[i]))
                }
            }
        }
        if (animators.isEmpty()) {
            for (view in extraBlockViews) {
                view.alpha = 0f
            }
            performHideShape()
        } else {
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animators)
            animatorSet.addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) = Unit

                override fun onAnimationEnd(animation: Animator) {
                    performHideShape()
                }

                override fun onAnimationCancel(animation: Animator) = Unit

                override fun onAnimationRepeat(animation: Animator) = Unit

            })
            animatorSet.start()
        }
    }

    private fun performHideShape() {
        val animators: MutableList<Animator> = mutableListOf()
        if (hideShapeAnimator != ShapeAnimator.NO_ANIMATOR) {
            hideShapeAnimator?.let {
                animators.add(it.getAnimator(this, shape))
            }
            //animators.add(hideShapeAnimator.getAnimator(this, shape))
        }
        if (animators.isEmpty()) {
            close()
        } else {
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(animators)
            animatorSet.addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) = Unit

                override fun onAnimationEnd(animation: Animator) {
                    close()
                }

                override fun onAnimationCancel(animation: Animator) = Unit

                override fun onAnimationRepeat(animation: Animator) = Unit
            })
            animatorSet.start()
        }
    }

    private fun close() {
        removeView()
        clearData()
        onClosedListener?.onClosed()
    }

    private fun clearData() {
        bitmap?.recycle()
        bitmap = null
        parent = null
        hintCase = null
    }

    private fun removeView() {
        parent?.removeView(this)
    }

    private fun setViews() {
        if (existHintBlock()) {
            val frameLayout = getHintBlockFrameLayout()
            if (hintBlockView == NO_BLOCK_INFO_VIEW) {
                hintBlockView = hintBlock?.getView(context, hintCase!!, frameLayout)
                hintBlockView?.alpha = 0f
            }
            frameLayout.addView(hintBlockView)
            addView(frameLayout)
        }
        if (existExtraBlock()) {
            val relativeLayout = getExtraContentHolderRelativeLayout()
            for (i in extraBlocks.indices) {
                val view = extraBlocks[i].getView(context, hintCase!!, relativeLayout)
                if (showExtraContentHolderAnimators[i] != ContentHolderAnimator.NO_ANIMATOR) {
                    view.alpha = 0f
                }
                extraBlockViews.add(view)
                relativeLayout.addView(view)
            }
            addView(relativeLayout)
        }
    }

    private fun existHintBlock(): Boolean = hintBlock != NO_BLOCK_INFO

    private fun existExtraBlock(): Boolean = extraBlocks.isNotEmpty()

    private fun getHintBlockFrameLayout(): FrameLayout {
        var blockWidth: Int = 0
        var blockHeight: Int = 0
        var blockAlign: Int = 0
        when(hintBlockPosition) {
            HintCase.HINT_BLOCK_POSITION_TOP -> {
                blockWidth = parent?.width ?: 0
                blockHeight = shape.top - (parent?.top ?: 0) - DimenUtils.getStatusBarHeight(activity)
                blockAlign = ALIGN_PARENT_TOP
            }
            HintCase.HINT_BLOCK_POSITION_BOTTOM -> {
                blockWidth = parent?.width ?: 0
                blockHeight = (parent?.bottom ?: 0) - navigationBarSizeIfExistAtTheBottom.y - shape.bottom
                blockAlign = ALIGN_PARENT_BOTTOM
            }
            HintCase.HINT_BLOCK_POSITION_LEFT -> {
                blockWidth = shape.left - (parent?.left ?: 0)
                blockHeight = (parent?.height ?: 0) - DimenUtils.getStatusBarHeight(activity)
                blockAlign = ALIGN_PARENT_LEFT
            }
            HintCase.HINT_BLOCK_POSITION_RIGHT -> {
                blockWidth = (parent?.getRight() ?: 0) - navigationBarSizeIfExistAtTheRight.x - shape.right
                blockHeight = (parent?.height ?: 0) - DimenUtils.getStatusBarHeight(activity)
                blockAlign = ALIGN_PARENT_RIGHT
            }
            HintCase.HINT_BLOCK_POSITION_CENTER -> {
                blockWidth = (parent?.getRight() ?: 0) - navigationBarSizeIfExistAtTheRight.x
                blockHeight = (parent?.height ?: 0) - navigationBarSizeIfExistAtTheBottom.y - DimenUtils.getStatusBarHeight(activity)
                blockAlign = ALIGN_PARENT_BOTTOM
            }
        }
        val relativeLayoutParams = LayoutParams(blockWidth, blockHeight)
        relativeLayoutParams.addRule(blockAlign)
        relativeLayoutParams.topMargin = DimenUtils.getStatusBarHeight(activity)
        relativeLayoutParams.bottomMargin = navigationBarSizeIfExistAtTheBottom.y
        relativeLayoutParams.rightMargin = navigationBarSizeIfExistAtTheRight.x
        val frameLayout = FrameLayout(context)
        frameLayout.layoutParams = relativeLayoutParams
        return frameLayout
    }

    private fun getExtraContentHolderRelativeLayout(): RelativeLayout {
        val relativeLayoutParams = LayoutParams(parent?.width ?: 0, parent?.height ?: 0)
        val relativeLayout = RelativeLayout(context)
        relativeLayoutParams.topMargin = DimenUtils.getStatusBarHeight(activity)
        relativeLayoutParams.bottomMargin = navigationBarSizeIfExistAtTheBottom.y
        relativeLayoutParams.rightMargin = navigationBarSizeIfExistAtTheRight.x
        relativeLayout.layoutParams = relativeLayoutParams
        return relativeLayout
    }

    private fun calculateHintBlockPosition(parent: ViewGroup, shape: Shape) {
        if (targetView == NO_TARGET_VIEW) {
            hintBlockPosition = HintCase.HINT_BLOCK_POSITION_CENTER
        } else {
            val areas = IntArray(4)
            areas[HintCase.HINT_BLOCK_POSITION_LEFT] = shape.left - parent.left
            areas[HintCase.HINT_BLOCK_POSITION_TOP] = shape.top - parent.top
            areas[HintCase.HINT_BLOCK_POSITION_RIGHT] = parent.right - shape.right
            areas[HintCase.HINT_BLOCK_POSITION_BOTTOM] = parent.bottom - shape.bottom
            hintBlockPosition = HintCase.HINT_BLOCK_POSITION_LEFT
            for (i in areas.indices) {
                if (areas[i] >= areas[hintBlockPosition]) {
                    hintBlockPosition = i
                }
            }
        }
    }

    fun setOnClosedListener(onClickListener: HintCase.OnClosedListener) {
        this.onClosedListener = onClickListener
    }

    fun setTargetInfo(view: View?, shape: Shape, offsetInPx: Int, isTargetClickable: Boolean) {
        this.targetView = view
        this.shape = shape
        this.offsetInPx = offsetInPx
        this.isTargetClickable = isTargetClickable
    }

    fun setOverDecorView(decorView: View) {
        parent = decorView as ViewGroup
        parentIndex = -1
    }

    fun setParentView(parentView: View) {
        parent = parentView as ViewGroup
        parentIndex = parent?.childCount ?: -1
    }

    fun setCloseOnTouch(closeOnTouch: Boolean) {
        this.closeOnTouch = closeOnTouch
    }

    fun show() {
        initializeView()
        performShow()
    }

    fun initializeView() {
        shape.setShapeInfo(targetView!!, parent!!, offsetInPx, context)
        calculateHintBlockPosition(parent!!, shape)
        setViews()
        buildBaseBitmap()
    }

    fun getShape() = shape

    fun setShape(showShapeAnimator: ShapeAnimator, hideShapeAnimator: ShapeAnimator?) {
        this.showShapeAnimator = showShapeAnimator
        this.hideShapeAnimator = hideShapeAnimator
    }

    fun setHintBlock(
        contentHolder: ContentHolder,
        showContentHolderAnimator: ContentHolderAnimator?,
        hideContentHolderAnimator: ContentHolderAnimator?
    ) {
        this.hintBlock = contentHolder
        this.showContentHolderAnimator = showContentHolderAnimator
        this.hideContentHolderAnimator = hideContentHolderAnimator
    }

    fun getHintBlockPosition() = hintBlockPosition

    fun setExtraBlock(
        contentHolder: ContentHolder,
        showContentHolderAnimator: ContentHolderAnimator?,
        hideContentHolderAnimator: ContentHolderAnimator?
    ) {
        this.extraBlocks.add(contentHolder)
        this.showExtraContentHolderAnimators.add(showContentHolderAnimator)
        this.hideExtraContentHolderAnimators.add(hideContentHolderAnimator)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (hintBlock != NO_BLOCK_INFO) {
            hintBlock?.onLayout()
        }

        for (extraBlock in extraBlocks) {
            extraBlock.onLayout()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val consumeTouchEvent = true
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                wasPressedOnShape = shape.isTouchEventInsideTheHint(event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (!shape.isTouchEventInsideTheHint(event)) {
                    wasPressedOnShape = false
                }
            }
            MotionEvent.ACTION_UP -> {
                if (closeOnTouch) {
                    performHide()
                }
                if (targetView != null
                    && isTargetClickable
                    && wasPressedOnShape
                    && shape.isTouchEventInsideTheHint(event)) {
                    targetView?.performClick()
                }
            }
        }
        return consumeTouchEvent
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (bitmap == null) {
            super.dispatchDraw(canvas)
        } else {
            bitmap?.eraseColor(backgroundColor)
            if (shape != null
                && (showShapeAnimator != ShapeAnimator.NO_ANIMATOR
                && hideShapeAnimator != ShapeAnimator.NO_ANIMATOR)) {
                val canvasShape = Canvas(bitmap!!)
                shape.draw(canvasShape)
            }
            canvas.drawBitmap(bitmap!!, 0f, 0f, basePaint)
            super.dispatchDraw(canvas)
        }
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
    }

    companion object {
        private const val DEFAULT_BACKGROUND_COLOR: Int = -0x34000000
        private const val DEFAULT_HINT_BLOCK_POSITION = HintCase.HINT_BLOCK_POSITION_BOTTOM
    }
}