package com.rodgim.movies.ui.discovery.hintcase.hintcontentholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rodgim.movies.ui.discovery.hintcase.HintCase

class ByLayoutHintContentHolder(var resLayoutId: Int) : HintContentHolder() {
    override fun getView(context: Context, hintCase: HintCase, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(resLayoutId, parent, false)
        return view
    }
}