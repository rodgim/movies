package com.rodgim.movies.ui.discovery.hintcase.extracontentholders

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatButton
import com.rodgim.movies.ui.discovery.hintcase.HintCase

class SimpleButtonContentHolder internal constructor() : ExtraContentHolder() {
    private var width = 0
    private var height = 0
    private var rules: IntArray = intArrayOf()
    private var text: CharSequence? = null
    private var buttonStyleId = 0
    private var closeHintOnClick = false
    private var onClickButtonListener: OnClickButtonListener? = null

    override fun getView(context: Context, hintCase: HintCase, parent: ViewGroup): View {
        val button: AppCompatButton
        if (buttonStyleId != 0) {
            val contextThemeWrapper: ContextThemeWrapper =
                ContextThemeWrapper(context, buttonStyleId)
            button = AppCompatButton(contextThemeWrapper)
        } else {
            button = AppCompatButton(context)
        }
        button.setText(text)
        button.setOnClickListener(View.OnClickListener {
            if (onClickButtonListener != null) {
                onClickButtonListener!!.onClick()
            }
            if (closeHintOnClick) {
                hintCase.hide()
            }
        })
        button.setLayoutParams(getParentLayoutParams(width, height, *rules))
        return button
    }

    class Builder(private val context: Context) {
        private val buttonBlock = SimpleButtonContentHolder()

        init {
            buttonBlock.width = ViewGroup.LayoutParams.WRAP_CONTENT
            buttonBlock.height = ViewGroup.LayoutParams.WRAP_CONTENT
            buttonBlock.rules = IntArray(0)
        }

        fun setWidth(width: Int): Builder {
            buttonBlock.width = width
            return this
        }

        fun setHeight(height: Int): Builder {
            buttonBlock.height = height
            return this
        }

        fun setRules(vararg rules: Int): Builder {
            buttonBlock.rules = rules
            return this
        }

        fun setButtonText(text: CharSequence?): Builder {
            buttonBlock.text = text
            return this
        }

        fun setButtonText(resId: Int): Builder {
            buttonBlock.text = context.getString(resId)
            return this
        }

        fun setButtonStyle(resId: Int): Builder {
            buttonBlock.buttonStyleId = resId
            return this
        }

        fun setOnClick(listener: OnClickButtonListener?): Builder {
            buttonBlock.onClickButtonListener = listener
            return this
        }

        fun setCloseHintCaseOnClick(closeHintOnClick: Boolean): Builder {
            buttonBlock.closeHintOnClick = closeHintOnClick
            return this
        }

        fun build(): SimpleButtonContentHolder {
            return buttonBlock
        }
    }

    interface OnClickButtonListener {
        fun onClick()
    }
}