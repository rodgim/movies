package com.rodgim.movies.ui.discovery.contentholder

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.marginLeft
import com.rodgim.movies.ui.common.setMargins
import com.rodgim.movies.ui.discovery.hintcase.HintCase
import com.rodgim.movies.ui.discovery.hintcase.extracontentholders.ExtraContentHolder

class SimpleIconContentHolder: ExtraContentHolder() {
    private var width: Int = 0
    private var height: Int = 0
    private var rules: IntArray = intArrayOf()
    private var text: CharSequence? = null
    private var iconStyleId: Int = 0
    private var imageSrcId: Int = -1
    private var adjustViewBounds: Boolean = true
    private var scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE
    private var topMargin: Int = 0
    private var bottomMargin: Int = 0
    private var rightMargin: Int = 0
    private var leftMargin: Int = 0
    private var closeHintOnClick: Boolean = false
    private var onClickIconListener: OnClickIconListener? = null

    override fun getView(context: Context, hintCase: HintCase, parent: ViewGroup): View {
        var icon: ImageView = if (iconStyleId != 0){
            val contextThemeWrapper = ContextThemeWrapper(context, iconStyleId)
            ImageView(contextThemeWrapper)
        }else{
            ImageView(context!!)
        }

        icon.setOnClickListener {
            if (onClickIconListener != null){
                onClickIconListener?.onClick()
            }
            if (closeHintOnClick){
                hintCase?.hide()
            }
        }

        icon.setImageResource(imageSrcId)
        icon.adjustViewBounds = adjustViewBounds
        icon.scaleType = scaleType
        icon.layoutParams = getParentLayoutParams(width, height, *rules)
        icon.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)

        return icon
    }

    class Builder(private val context: Context){
        private val iconBlock: SimpleIconContentHolder = SimpleIconContentHolder()

        init {
            this.iconBlock.width = ViewGroup.LayoutParams.WRAP_CONTENT
            this.iconBlock.height = ViewGroup.LayoutParams.WRAP_CONTENT
            this.iconBlock.rules = intArrayOf(0)
        }

        fun setWidth(width: Int): Builder {
            iconBlock.width = width
            return this
        }

        fun setHeight(height: Int): Builder {
            iconBlock.height = height
            return this
        }

        fun setRules(vararg rules: Int): Builder {
            iconBlock.rules = rules
            return this
        }

        fun setButtonText(text: CharSequence): Builder {
            iconBlock.text = text
            return this
        }

        fun setImageResourceId(resId: Int): Builder {
            iconBlock.imageSrcId = resId
            return this
        }

        fun setIconStyle(resId: Int): Builder {
            iconBlock.iconStyleId = resId
            return this
        }

        fun setIconAdjustViewBounds(adjustViewBounds: Boolean): Builder {
            iconBlock.adjustViewBounds = adjustViewBounds
            return this
        }

        fun setScaleType(scaleType: ImageView.ScaleType): Builder {
            iconBlock.scaleType = scaleType
            return this
        }

        fun setMargin(left: Int, top: Int, right: Int, bottom: Int): Builder {
            iconBlock.leftMargin = left
            iconBlock.topMargin = top
            iconBlock.rightMargin = right
            iconBlock.bottomMargin = bottom
            return this
        }

        fun setMarginByResourcesId(
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ): Builder {
            iconBlock.leftMargin = context.resources.getDimensionPixelSize(left)
            iconBlock.topMargin = context.resources.getDimensionPixelSize(top)
            iconBlock.rightMargin = context.resources.getDimensionPixelSize(right)
            iconBlock.bottomMargin = context.resources.getDimensionPixelSize(bottom)
            return this
        }

        fun setOnClick(listener: OnClickIconListener): Builder {
            iconBlock.onClickIconListener = listener
            return this
        }

        fun setCloseHintCaseOnClick(closeHintOnClick: Boolean): Builder {
            iconBlock.closeHintOnClick = closeHintOnClick
            return this
        }

        fun build(): SimpleIconContentHolder {
            return iconBlock
        }
    }

    interface OnClickIconListener{
        fun onClick()
    }
}