package com.rodgim.movies.ui.discovery.hintcase.hintcontentholders

import android.widget.FrameLayout
import com.rodgim.movies.ui.discovery.hintcase.ContentHolder

abstract class HintContentHolder : ContentHolder() {
    fun getParentLayoutParams(width: Int, height: Int, gravity: Int): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(width, height, gravity)
    }

    fun getParentLayoutParams(
        width: Int, height: Int, gravity: Int,
        marginLeft: Int, marginTop: Int,
        marginRight: Int, marginBottom: Int,
    ): FrameLayout.LayoutParams {
        val layoutParams = FrameLayout.LayoutParams(width, height, gravity)
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        return layoutParams
    }
}