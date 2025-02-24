package com.rodgim.movies.ui.discovery.contentholder

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.rodgim.movies.R
import com.rodgim.movies.ui.discovery.TriangleShapeView
import com.rodgim.movies.ui.discovery.hintcase.HintCase
import com.rodgim.movies.ui.discovery.hintcase.hintcontentholders.HintContentHolder
import com.rodgim.movies.ui.discovery.hintcase.utils.DimenUtils

class DialogHintContentHolder(val activity: Activity): HintContentHolder() {
    companion object{
        private const val NO_IMAGE = -1
        const val BACKGROUND_COLOR_TRANSPARENT = 0x00000000
        const val DEFAULT_ARROW_SIZE_IN_PX = 50
    }

    private var borderColor: Int = BACKGROUND_COLOR_TRANSPARENT
    private var backgroundColor: Int = BACKGROUND_COLOR_TRANSPARENT
    private var arrowBackgroundColor: Int = R.color.delivered
    private var contentBackgroundColor: Int = R.color.delivered
    private var borderSize: Int? = null
    private var imageView: ImageView? = null
    private var imageResourceId: Int? = null
    private var contentTitle: CharSequence? = null
    private var contentText: CharSequence? = null
    private var titleStyleId: Int? = null
    private var textStyleId: Int? = null
    private var marginLeft: Int = 0
    private var marginTop: Int = 0
    private var marginRight: Int = 0
    private var marginBottom: Int = 0
    private var alignBlockRules: ArrayList<Int> = ArrayList()
    private var alignArrowRules: ArrayList<Int> = ArrayList()
    private var contentTopMargin: Int = 0
    private var contentBottomMargin: Int = 0
    private var contentRightMargin: Int = 0
    private var contentLeftMargin: Int = 0
    private var contentTopPadding: Int = 0
    private var contentBottomPadding: Int = 0
    private var contentRightPadding: Int = 0
    private var contentLeftPadding: Int = 0
    private var gravity: Int = Gravity.CENTER
    private var xTranslationImage: Float = 0f
    private var yTranslationImage: Float = 0f
    private var xTranslationContent: Float = 0f
    private var arrow: TriangleShapeView? = null
    private var hintCase: HintCase? = null
    private var parent: ViewGroup? = null
    private lateinit var contentLinearLayout: LinearLayout
    private var arrowWidth: Int? = null
    private var arrowHeight: Int? = null
    private var shadowSize: Int? = null
    private var useBorder: Boolean = false
    private var arrowDirection: TriangleShapeView.Direction? = null
    private var extraTargetHeight: Int = 0

    override fun getView(context: Context, hintCase: HintCase, parent: ViewGroup): View {
        this.hintCase = hintCase
        this.parent = parent

        calculateDataToPutTheArrow(hintCase!!)
        setArrow(context)

        val frameLayoutParamsBlock = getParentLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            gravity, marginLeft, marginTop, marginRight, marginBottom
        )

        val fullBlockLayout = RelativeLayout(context)
        fullBlockLayout.layoutParams = frameLayoutParamsBlock

        val relativeLayoutParamsLinear = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        for (rule in alignBlockRules){
            relativeLayoutParamsLinear.addRule(rule)
        }
        relativeLayoutParamsLinear.topMargin = contentTopMargin
        relativeLayoutParamsLinear.bottomMargin = contentBottomMargin
        relativeLayoutParamsLinear.rightMargin = contentRightMargin
        relativeLayoutParamsLinear.leftMargin = contentLeftMargin

        contentLinearLayout = LinearLayout(context)
        contentLinearLayout.setBackgroundResource(R.drawable.bubble_border_background)

        val layerDrawable = contentLinearLayout.background?.current as LayerDrawable
        val gradientDrawable =
            layerDrawable.getDrawable(layerDrawable.numberOfLayers - 1) as GradientDrawable
        gradientDrawable.setColor(context!!.resources.getColor(contentBackgroundColor))
        if (useBorder){
            gradientDrawable.setStroke(borderSize!!, borderColor)
        }

        contentLinearLayout.layoutParams = relativeLayoutParamsLinear
        contentLinearLayout.gravity = Gravity.CENTER
        val padding = borderSize!! + shadowSize!!
        contentLinearLayout.setPadding(
            padding + contentLeftPadding,
            padding + contentTopPadding,
            padding + contentRightPadding,
            padding + contentBottomPadding
        )
        contentLinearLayout.orientation = LinearLayout.VERTICAL

        if (contentTitle != null){
            contentLinearLayout.addView(getTextViewTitle(context))
        }
        if (existImage()){
            contentLinearLayout.addView(getImage(context, imageView, imageResourceId!!))
        }
        if (contentText != null){
            contentLinearLayout.addView(getTextViewDescription(context))
        }
        fullBlockLayout.addView(contentLinearLayout)
        fullBlockLayout.addView(arrow)
        return fullBlockLayout
    }

    override fun onLayout() {
        calculateArrowTranslation()
        arrow?.translationX = xTranslationImage
        arrow?.translationY = yTranslationImage
        calculateContentTranslation()
        contentLinearLayout.translationX = xTranslationContent

        if (hintCase?.getBlockInfoPosition() == HintCase.HINT_BLOCK_POSITION_RIGHT
            || hintCase?.getBlockInfoPosition() == HintCase.HINT_BLOCK_POSITION_LEFT){
            if (arrow?.bottom ?: 0 >= contentLinearLayout.bottom ?: 0){
                val translationY = arrow!!.y + (arrow!!.height / 2) - contentLinearLayout.y - (contentLinearLayout.height /2)
                contentLinearLayout.translationY = translationY
            }
        }
    }

    private fun setArrow(context: Context?){
        val relativeLayoutParamsArrow = RelativeLayout.LayoutParams(arrowWidth!!, arrowHeight!!)
        for (rule in alignArrowRules){
            relativeLayoutParamsArrow.addRule(rule)
        }
        arrow = TriangleShapeView(context!!)
        arrow?.setTriangleBackgroundColor(context.resources.getColor(arrowBackgroundColor))
        if (useBorder){
            arrow?.setBorder(borderSize!!, borderColor)
        }
        arrow?.setDirection(arrowDirection!!)
        arrow?.setShadowSize(shadowSize!!)
        arrow?.layoutParams = relativeLayoutParamsArrow
    }

    private fun calculateContentTranslation(){
        when(hintCase?.getBlockInfoPosition()){
            HintCase.HINT_BLOCK_POSITION_TOP, HintCase.HINT_BLOCK_POSITION_BOTTOM -> {
                val contentWidth =
                    contentLinearLayout.width + contentLeftMargin + contentRightMargin + marginLeft + marginRight
                val halfContentWidth = contentWidth / 2
                if (((hintCase!!.getShape()?.centerX ?: 0f) + halfContentWidth) > parent!!.width) {
                    xTranslationContent = (parent!!.width - contentWidth).toFloat()
                }else if(((hintCase!!.getShape()?.centerX ?: 0f) - halfContentWidth) < 0){
                    xTranslationContent = 0f
                }else{
                    xTranslationContent = ((hintCase!!.getShape()?.centerX ?: 0f)) - halfContentWidth
                }
            }
        }
    }


    private fun calculateArrowTranslation(){
        when(hintCase?.getBlockInfoPosition()){
            HintCase.HINT_BLOCK_POSITION_TOP, HintCase.HINT_BLOCK_POSITION_BOTTOM -> {
                xTranslationImage = (hintCase!!.getShape()?.centerX ?: 0f) - (parent!!.width / 2)
                yTranslationImage = 0f
            }
            HintCase.HINT_BLOCK_POSITION_RIGHT, HintCase.HINT_BLOCK_POSITION_LEFT -> {
                xTranslationImage = 0f
                yTranslationImage = (hintCase!!.getShape()?.centerY ?: 0f) - (parent!!.height / 2) - DimenUtils.getStatusBarHeight(activity)
            }
            else -> {
                xTranslationImage = 0f
                yTranslationImage = 0f
            }
        }
    }

    private fun calculateDataToPutTheArrow(hintCase: HintCase){
        when(hintCase.getBlockInfoPosition()){
            HintCase.HINT_BLOCK_POSITION_TOP -> {
                alignBlockRules.add(RelativeLayout.ALIGN_PARENT_BOTTOM)
                alignArrowRules.add(RelativeLayout.CENTER_HORIZONTAL)
                alignArrowRules.add(RelativeLayout.ALIGN_PARENT_BOTTOM)
                gravity = Gravity.BOTTOM
                contentRightMargin = 0
                contentLeftMargin = 0
                contentTopMargin = 0
                contentBottomMargin = arrowHeight!! - borderSize!! - shadowSize!!
                arrowDirection = TriangleShapeView.Direction.DOWN
                marginBottom = if (extraTargetHeight != 0){
                    extraTargetHeight - marginBottom + contentBottomMargin
                }else{
                    0
                }
            }
            HintCase.HINT_BLOCK_POSITION_BOTTOM -> {
                alignBlockRules.add(RelativeLayout.ALIGN_PARENT_TOP)
                alignArrowRules.add(RelativeLayout.CENTER_HORIZONTAL)
                alignArrowRules.add(RelativeLayout.ALIGN_PARENT_TOP)
                gravity = Gravity.TOP
                contentRightMargin = 0
                contentLeftMargin = 0
                contentTopMargin = arrowHeight!! - borderSize!! - shadowSize!!
                contentBottomMargin = 0
                arrowDirection = TriangleShapeView.Direction.UP
                marginTop = 0
            }
            HintCase.HINT_BLOCK_POSITION_RIGHT -> {
                alignBlockRules.add(RelativeLayout.ALIGN_PARENT_LEFT)
                alignArrowRules.add(RelativeLayout.CENTER_VERTICAL)
                alignArrowRules.add(RelativeLayout.ALIGN_PARENT_LEFT)
                gravity = Gravity.LEFT
                contentRightMargin = 0
                contentLeftMargin = arrowHeight!!
                contentTopMargin = 0
                contentBottomMargin = 0
                arrowDirection = TriangleShapeView.Direction.LEFT
                marginLeft = 0
            }
            HintCase.HINT_BLOCK_POSITION_LEFT -> {
                alignBlockRules.add(RelativeLayout.ALIGN_PARENT_RIGHT)
                alignArrowRules.add(RelativeLayout.ALIGN_PARENT_RIGHT)
                alignArrowRules.add(RelativeLayout.CENTER_VERTICAL)
                gravity = Gravity.RIGHT
                contentRightMargin = arrowHeight!!
                contentLeftMargin = 0
                contentTopMargin = 0
                contentBottomMargin = 0
                arrowDirection = TriangleShapeView.Direction.RIGHT
                marginRight = 0
            }
            else -> {
                alignBlockRules.add(RelativeLayout.CENTER_HORIZONTAL)
                alignArrowRules.add(RelativeLayout.CENTER_IN_PARENT)
                gravity = Gravity.CENTER
                contentRightMargin = 0
                contentLeftMargin = 0
                contentTopMargin = 0
                contentBottomMargin = 0
                xTranslationImage = 0f
                yTranslationImage = 0f
            }
        }

    }

    private fun getImage(context: Context?, imageView: ImageView?, imageResourceId: Int): ImageView {
        val linearLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
        linearLayoutParams.setMargins(0, 20, 0,50)
        val image = imageView ?: ImageView(context)
        if (imageResourceId != NO_IMAGE){
            image.setImageResource(imageResourceId)
        }
        image.scaleType = ImageView.ScaleType.CENTER_INSIDE
        image.adjustViewBounds = true
        image.layoutParams = linearLayoutParams
        return image
    }

    private fun getTextViewTitle(context: Context?): View {
        val linearLayoutParamsTitle = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayoutParamsTitle.setMargins(0, 0, 0, 20)
        val textViewTitle = TextView(context)
        textViewTitle.layoutParams = linearLayoutParamsTitle
        textViewTitle.gravity = Gravity.CENTER_HORIZONTAL
        val spannableStringTitle = SpannableString(contentTitle)
        val titleTextAppearanceSpan = TextAppearanceSpan(context, titleStyleId!!)
        spannableStringTitle.setSpan(titleTextAppearanceSpan, 0, spannableStringTitle.length, 0)
        textViewTitle.setText(spannableStringTitle)
        return textViewTitle
    }

    private fun getTextViewDescription(context: Context?): View {
        val linearLayoutParamsText = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val textViewDescription = TextView(context)
        textViewDescription.layoutParams = linearLayoutParamsText
        textViewDescription.gravity = Gravity.CENTER_HORIZONTAL
        val spannableStringText = SpannableString(contentText)
        val descriptionTextAppearanceSpan = TextAppearanceSpan(context, textStyleId!!)
        spannableStringText.setSpan(descriptionTextAppearanceSpan, 0, spannableStringText.length, 0)
        textViewDescription.setText(spannableStringText)
        return textViewDescription
    }

    private fun existImage(): Boolean{
        return imageView != null || imageResourceId != NO_IMAGE
    }

    class Builder(private val activity: Activity){
        private val blockInfo: DialogHintContentHolder = DialogHintContentHolder(activity)

        init {
            this.blockInfo.imageResourceId = NO_IMAGE
            this.blockInfo.arrowWidth = DEFAULT_ARROW_SIZE_IN_PX
            this.blockInfo.arrowHeight = DEFAULT_ARROW_SIZE_IN_PX
            this.blockInfo.useBorder = false
            this.blockInfo.shadowSize = activity.resources.getDimensionPixelSize(R.dimen.hint_shadow)
        }

        fun setBorder(resourceId: Int, resId: Int): Builder {
            blockInfo.useBorder = true
            blockInfo.borderSize = activity.resources.getDimensionPixelSize(resourceId)
            blockInfo.borderColor = activity.resources.getColor(resId)
            return this
        }

        fun setBackgroundColorFromResource(resId: Int): Builder {
            blockInfo.backgroundColor = activity.resources.getColor(resId)
            return this
        }

        fun setBackgroundColor(color: Int): Builder {
            blockInfo.backgroundColor = color
            return this
        }

        fun setImageDrawableId(resourceId: Int): Builder {
            blockInfo.imageResourceId = resourceId
            return this
        }

        fun setImageView(imageView: ImageView): Builder {
            blockInfo.imageView = imageView
            return this
        }

        fun setContentTitle(title: CharSequence): Builder {
            blockInfo.contentTitle = title
            return this
        }

        fun setTitleStyle(resId: Int): Builder {
            blockInfo.titleStyleId = resId
            return this
        }

        fun setContentText(text: CharSequence): Builder {
            blockInfo.contentText = text
            return this
        }

        fun setContentText(resId: Int): Builder {
            blockInfo.contentText = activity.getString(resId)
            return this
        }

        fun setContentStyle(resId: Int): Builder {
            blockInfo.textStyleId = resId
            return this
        }

        fun setMargin(left: Int, top: Int, right: Int, bottom: Int): Builder {
            blockInfo.marginLeft = left
            blockInfo.marginTop = top
            blockInfo.marginRight = right
            blockInfo.marginBottom = bottom
            return this
        }

        fun setMarginByResourcesId(
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ): Builder {
            blockInfo.marginLeft = activity.resources.getDimensionPixelSize(left)
            blockInfo.marginTop = activity.resources.getDimensionPixelSize(top)
            blockInfo.marginRight = activity.resources.getDimensionPixelSize(right)
            blockInfo.marginBottom = activity.resources.getDimensionPixelSize(bottom)
            return this
        }

        fun setContentPadding(left: Int, top: Int, right: Int, bottom: Int): Builder {
            blockInfo.contentLeftPadding = left
            blockInfo.contentTopPadding = top
            blockInfo.contentRightPadding = right
            blockInfo.contentBottomPadding = bottom
            return this
        }

        fun setContentPaddingByResourcesId(
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ): Builder {
            blockInfo.contentLeftPadding = activity.resources.getDimensionPixelSize(left)
            blockInfo.contentTopPadding = activity.resources.getDimensionPixelSize(top)
            blockInfo.contentRightPadding = activity.resources.getDimensionPixelSize(right)
            blockInfo.contentBottomPadding = activity.resources.getDimensionPixelSize(bottom)
            return this
        }

        fun setContentBackground(colorResId: Int): Builder {
            blockInfo.contentBackgroundColor = colorResId
            return this
        }

        fun setArrowSize(widthResId: Int, heightResId: Int): Builder {
            blockInfo.arrowWidth = activity.resources.getDimensionPixelSize(widthResId)
            blockInfo.arrowHeight = activity.resources.getDimensionPixelSize(heightResId)
            return this
        }

        fun setArrowBackground(colorResId: Int): Builder {
            blockInfo.arrowBackgroundColor = colorResId
            return this
        }

        fun setExtraTargetHeight(height: Int): Builder {
            blockInfo.extraTargetHeight = height
            return this
        }

        fun build(): DialogHintContentHolder {
            return blockInfo
        }
    }
}