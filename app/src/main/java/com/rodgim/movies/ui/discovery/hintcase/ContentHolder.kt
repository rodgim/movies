package com.rodgim.movies.ui.discovery.hintcase

import android.content.Context
import android.view.View
import android.view.ViewGroup

abstract class ContentHolder {
    abstract fun getView(context: Context, hintCase: HintCase, parent: ViewGroup): View

    open fun onLayout(){}
}