package com.rodgim.movies.ui.discovery

import android.view.View

data class HintViewItem(
    val target1: View?,
    val target2: View? = null,
    val type: Type = Type.ONE_TARGET,
    val text: String
) {
    enum class Type{
        ONE_TARGET,
        TWO_TARGET
    }
}
