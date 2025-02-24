package com.rodgim.movies.ui.discovery

import android.view.View

data class HintItem(
    val target: View?,
    val group: Int,
    val type: HintViewItem.Type = HintViewItem.Type.TWO_TARGET
)
