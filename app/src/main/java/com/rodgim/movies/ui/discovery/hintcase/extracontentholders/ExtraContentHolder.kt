package com.rodgim.movies.ui.discovery.hintcase.extracontentholders

import android.widget.RelativeLayout
import com.rodgim.movies.ui.discovery.hintcase.ContentHolder

abstract class ExtraContentHolder : ContentHolder() {
    fun getParentLayoutParams(
        width: Int,
        height: Int,
        vararg rules: Int,
    ): RelativeLayout.LayoutParams {
        val layoutParams =
            RelativeLayout.LayoutParams(width, height)
        for (rule in rules) {
            layoutParams.addRule(rule)
        }
        return layoutParams
    }
}