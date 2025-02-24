package com.rodgim.movies.ui.discovery.hintcase.hintcontentholders

import android.content.Context
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.rodgim.movies.ui.discovery.hintcase.HintCase

class SimpleHintContentHolder : HintContentHolder() {
    private var imageView: ImageView? = null
    private var imageResourceId = 0
    private var contentTitle: CharSequence? = null
    private var contentText: CharSequence? = null
    private var titleStyleId = 0
    private var textStyleId = 0
    private var marginLeft = 0
    private var marginTop = 0
    private var marginRight = 0
    private var marginBottom = 0
    private var gravity = 0

    override fun getView(context: Context, hintCase: HintCase, parent: ViewGroup): View {
        val frameLayoutParamsBlock = getParentLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            gravity, marginLeft, marginTop, marginRight, marginBottom
        )
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = frameLayoutParamsBlock
        linearLayout.gravity = Gravity.CENTER
        linearLayout.orientation = LinearLayout.VERTICAL
        if (contentTitle != null) {
            linearLayout.addView(getTextViewTitle(context))
        }
        if (existImage()) {
            linearLayout.addView(getImage(context, imageView, imageResourceId))
        }
        if (contentText != null) {
            linearLayout.addView(getTextViewDescription(context))
        }
        return linearLayout
    }

    private fun getImage(context: Context, image: ImageView?, imageResourceId: Int): ImageView {
        var image = image
        val linearLayoutParamsImage =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
            )
        linearLayoutParamsImage.setMargins(0, 20, 0, 50)
        if (image == null) {
            image = ImageView(context)
        }
        if (imageResourceId != NO_IMAGE) {
            image.setImageResource(imageResourceId)
        }
        image.scaleType = ImageView.ScaleType.CENTER_INSIDE
        image.setAdjustViewBounds(true)
        image.setLayoutParams(linearLayoutParamsImage)
        return image
    }

    private fun getTextViewTitle(context: Context): View {
        val linearLayoutParamsTitle =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        linearLayoutParamsTitle.setMargins(0, 20, 0, 20)
        val textViewTitle = TextView(context)
        textViewTitle.layoutParams = linearLayoutParamsTitle
        textViewTitle.gravity = Gravity.CENTER_HORIZONTAL
        val spannableStringTitle = SpannableString(contentTitle)
        val titleTextAppearanceSpan = TextAppearanceSpan(context, titleStyleId)
        spannableStringTitle.setSpan(titleTextAppearanceSpan, 0, spannableStringTitle.length, 0)
        textViewTitle.text = spannableStringTitle
        return textViewTitle
    }

    private fun getTextViewDescription(context: Context): View {
        val linearLayoutParamsText =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        val textViewDescription = TextView(context)
        textViewDescription.layoutParams = linearLayoutParamsText
        textViewDescription.gravity = Gravity.CENTER_HORIZONTAL
        val spannableStringText = SpannableString(contentText)
        val textTextAppearanceSpan = TextAppearanceSpan(context, textStyleId)
        spannableStringText.setSpan(textTextAppearanceSpan, 0, spannableStringText.length, 0)
        textViewDescription.text = spannableStringText
        return textViewDescription
    }

    private fun existImage(): Boolean {
        return imageView != null ||
                imageResourceId != NO_IMAGE
    }

    class Builder(private val context: Context) {
        private val blockInfo = SimpleHintContentHolder()

        init {
            blockInfo.imageResourceId = NO_IMAGE
            blockInfo.gravity = Gravity.CENTER
        }

        fun setImageDrawableId(resourceId: Int): Builder {
            blockInfo.imageResourceId = resourceId
            return this
        }

        fun setImageView(imageView: ImageView?): Builder {
            blockInfo.imageView = imageView
            return this
        }

        fun setContentTitle(title: CharSequence?): Builder {
            blockInfo.contentTitle = title
            return this
        }

        fun setContentTitle(resId: Int): Builder {
            blockInfo.contentTitle = context.getString(resId)
            return this
        }

        fun setTitleStyle(resId: Int): Builder {
            blockInfo.titleStyleId = resId
            return this
        }

        fun setContentText(text: CharSequence?): Builder {
            blockInfo.contentText = text
            return this
        }

        fun setContentText(resId: Int): Builder {
            blockInfo.contentText = context.getString(resId)
            return this
        }

        fun setContentStyle(resId: Int): Builder {
            blockInfo.textStyleId = resId
            return this
        }

        fun setGravity(gravity: Int): Builder {
            blockInfo.gravity = gravity
            return this
        }

        fun setMargin(left: Int, top: Int, right: Int, bottom: Int): Builder {
            blockInfo.marginLeft = left
            blockInfo.marginTop = top
            blockInfo.marginRight = right
            blockInfo.marginBottom = bottom
            return this
        }

        fun setMarginByResourcesId(left: Int, top: Int, right: Int, bottom: Int): Builder {
            blockInfo.marginLeft = context.resources.getDimensionPixelSize(left)
            blockInfo.marginTop = context.resources.getDimensionPixelSize(top)
            blockInfo.marginRight = context.resources.getDimensionPixelSize(right)
            blockInfo.marginBottom = context.resources.getDimensionPixelSize(bottom)
            return this
        }

        fun build(): SimpleHintContentHolder {
            return blockInfo
        }
    }

    companion object {
        const val NO_IMAGE: Int = -1
    }
}