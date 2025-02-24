package com.rodgim.movies.ui.discovery.hintcase

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.rodgim.movies.ui.discovery.hintcase.shapeanimators.ShapeAnimator
import com.rodgim.movies.ui.discovery.hintcase.shapes.RectangularShape
import com.rodgim.movies.ui.discovery.hintcase.shapes.Shape
import com.rodgim.movies.ui.discovery.hintcase.utils.DimenUtils

class HintCase(view: View, activity: Activity) {
    private var hintCaseView: HintCaseView? = null
    private val context: Context = view.context

    init {
        this.hintCaseView = HintCaseView(activity, this)
        this.hintCaseView?.setTargetInfo(null, RectangularShape(), NO_OFFSET_IN_PX.toInt(), TARGET_IS_NOT_CLICKABLE)
        this.hintCaseView?.setParentView(view)
    }

    fun getShape() = this.hintCaseView?.getShape()

    fun getView() = this.hintCaseView

    fun setTarget(target: View): HintCase {
        val offSetInPx = DimenUtils.dipToPixels(context, DEFAULT_SHAPE_OFFSET_IN_DP)
        return setCompleteTarget(target, RectangularShape(), offSetInPx, TARGET_IS_CLICKABLE)
    }

    fun setTarget(target: View, @DimenRes offsetResId: Int): HintCase {
        val offsetInPx = context.resources.getDimensionPixelSize(offsetResId)
        return setCompleteTarget(target, RectangularShape(), offsetInPx, TARGET_IS_CLICKABLE)
    }

    fun setTarget(target: View, isTargetClickable: Boolean): HintCase {
        val offsetInPx = DimenUtils.dipToPixels(context, DEFAULT_SHAPE_OFFSET_IN_DP)
        return setCompleteTarget(target, RectangularShape(), offsetInPx, isTargetClickable)
    }

    fun setTarget(target: View, isTargetClickable: Boolean, @DimenRes offsetResId: Int): HintCase {
        val offsetInPx = context.resources.getDimensionPixelSize(offsetResId)
        return setCompleteTarget(target, RectangularShape(), offsetInPx, isTargetClickable)
    }

    fun setTarget(target: View, shape: Shape): HintCase {
        val offsetInPx = DimenUtils.dipToPixels(context, DEFAULT_SHAPE_OFFSET_IN_DP)
        return setCompleteTarget(target, shape, offsetInPx, TARGET_IS_CLICKABLE)
    }

    fun setTarget(target: View, shape: Shape, @DimenRes offsetResId: Int): HintCase {
        val offsetInPx = context.resources.getDimensionPixelSize(offsetResId)
        return setCompleteTarget(target, shape, offsetInPx, TARGET_IS_CLICKABLE)
    }

    fun setTarget(target: View, shape: Shape, isTargetClickable: Boolean): HintCase {
        val offsetInPx = DimenUtils.dipToPixels(context, DEFAULT_SHAPE_OFFSET_IN_DP)
        return setCompleteTarget(target, shape, offsetInPx, isTargetClickable)
    }

    fun setTarget(target: View, shape: Shape, isTargetClickable: Boolean, @DimenRes offsetResId: Int): HintCase {
        val offsetInPx = context.resources.getDimensionPixelSize(offsetResId)
        return setCompleteTarget(target, shape, offsetInPx, isTargetClickable)
    }

    private fun setCompleteTarget(
        target: View,
        shape: Shape,
        offsetInPx: Int,
        isTargetClickable: Boolean
    ): HintCase {
        this.hintCaseView?.setTargetInfo(target, shape, offsetInPx, isTargetClickable)
        return this
    }

    fun setBackgroundColor(color: Int): HintCase {
        this.hintCaseView?.setBackgroundColor(color)
        return this
    }

    fun setBackgroundColorByResourceId(@ColorRes resourceId: Int): HintCase {
        this.hintCaseView?.setBackgroundColor(ContextCompat.getColor(context, resourceId))
        return this
    }

    fun setShapeAnimators(showShapeAnimator: ShapeAnimator): HintCase {
        return setShapeAnimators(showShapeAnimator, ShapeAnimator.NO_ANIMATOR)
    }

    fun setShapeAnimators(showShapeAnimator: ShapeAnimator, hideShapeAnimator: ShapeAnimator?): HintCase {
        this.hintCaseView?.setShape(showShapeAnimator, hideShapeAnimator)
        return this
    }

    fun setHintBlock(contentHolder: ContentHolder): HintCase {
        this.hintCaseView?.setHintBlock(contentHolder, ContentHolderAnimator.NO_ANIMATOR, ContentHolderAnimator.NO_ANIMATOR)
        return this
    }

    fun setHintBlock(
        contentHolder: ContentHolder,
        showContentHolderAnimator: ContentHolderAnimator
    ): HintCase {
        this.hintCaseView?.setHintBlock(contentHolder, showContentHolderAnimator, ContentHolderAnimator.NO_ANIMATOR)
        return this
    }

    fun setHintBlock(
        contentHolder: ContentHolder,
        showContentHolderAnimator: ContentHolderAnimator,
        hideContentHolderAnimator: ContentHolderAnimator
    ): HintCase {
        this.hintCaseView?.setHintBlock(contentHolder, showContentHolderAnimator, hideContentHolderAnimator)
        return this
    }

    fun setExtraBlock(contentHolder: ContentHolder): HintCase {
        this.hintCaseView?.setExtraBlock(contentHolder, ContentHolderAnimator.NO_ANIMATOR, ContentHolderAnimator.NO_ANIMATOR)
        return this
    }

    fun setExtraBlock(
        contentHolder: ContentHolder,
        showContentHolderAnimator: ContentHolderAnimator
    ): HintCase {
        this.hintCaseView?.setExtraBlock(contentHolder, showContentHolderAnimator, ContentHolderAnimator.NO_ANIMATOR)
        return this
    }

    fun setExtraBlock(
        contentHolder: ContentHolder,
        showContentHolderAnimator: ContentHolderAnimator,
        hideContentHolderAnimator: ContentHolderAnimator
    ): HintCase {
        this.hintCaseView?.setExtraBlock(contentHolder, showContentHolderAnimator, hideContentHolderAnimator)
        return this
    }

    fun setCloseOnTouchView(closeOnTouch: Boolean): HintCase {
        this.hintCaseView?.setCloseOnTouch(closeOnTouch)
        return this
    }

    fun setOverDecorView(setOver: Boolean, activity: Activity?): HintCase {
        if (activity != null) {
            setOverDecorView(setOver, activity.window.decorView)
        }
        return this
    }

    fun setOverDecorView(setOver: Boolean, decorView: View): HintCase {
        if (setOver) {
            this.hintCaseView?.setOverDecorView(decorView)
        }
        return this
    }

    fun setOnClosedListener(onClosedListener: OnClosedListener): HintCase {
        this.hintCaseView?.setOnClosedListener(onClosedListener)
        return this
    }

    fun getBlockInfoPosition(): Int {
        return this.hintCaseView?.getHintBlockPosition() ?: -1
    }

    fun show() {
        this.hintCaseView?.show()
    }

    fun hide() {
        this.hintCaseView?.performHide()
        this.hintCaseView = null
    }

    interface OnClosedListener {
        fun onClosed()
    }

    companion object {
        const val DEFAULT_SHAPE_OFFSET_IN_DP = 10f
        const val NO_OFFSET_IN_PX = 0f
        const val BACKGROUND_COLOR_TRANSPARENT = 0x00000000
        const val HINT_BLOCK_POSITION_LEFT = 0
        const val HINT_BLOCK_POSITION_TOP = 1
        const val HINT_BLOCK_POSITION_RIGHT = 2
        const val HINT_BLOCK_POSITION_BOTTOM = 3
        const val HINT_BLOCK_POSITION_CENTER = 4
        const val TARGET_IS_CLICKABLE = true
        const val TARGET_IS_NOT_CLICKABLE = false
    }
}