package com.rodgim.movies.ui.discovery.contentholder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.rodgim.movies.ui.common.getStatusBarHeight
import com.rodgim.movies.ui.discovery.hintcase.HintCase
import com.rodgim.movies.ui.discovery.hintcase.extracontentholders.ExtraContentHolder

class ExtraTargetContentHolder: ExtraContentHolder() {
    private var leftPadding: Int = 0
    private var topPadding: Int = 0
    private var rightPadding: Int = 0
    private var bottomPadding: Int = 0
    private var targetView: View? = null
    private var fullBlockLayout: RelativeLayout? = null
    private var width: Int = 0
    private var height: Int = 0
    private var image: ImageView? = null
    private var backgroundResourceId: Int = -1

    override fun getView(context: Context, hintCase: HintCase, parent: ViewGroup): View {

        val frameLayoutParamsBlock = getParentLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        fullBlockLayout = RelativeLayout(context)
        fullBlockLayout?.layoutParams = frameLayoutParamsBlock
        if (backgroundResourceId != -1){
            fullBlockLayout?.setBackgroundResource(backgroundResourceId)
        }else{
            fullBlockLayout?.setBackgroundColor(Color.WHITE)
        }

        image= ImageView(context!!)
        image?.adjustViewBounds = true
        image?.scaleType = ImageView.ScaleType.CENTER_INSIDE
        image?.setImageBitmap(getBitmap())

        fullBlockLayout?.addView(image)

        val locationInWindow = IntArray(2)
        targetView?.getLocationInWindow(locationInWindow)
        fullBlockLayout?.translationX = locationInWindow[0].toFloat() - leftPadding
        fullBlockLayout?.translationY = locationInWindow[1].toFloat() - (targetView?.getStatusBarHeight() ?: 0) - topPadding
        fullBlockLayout?.setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
        return fullBlockLayout!!
    }

    private fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(
            targetView!!.width,
            targetView!!.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        targetView?.draw(canvas)
        return bitmap
    }

    class Builder(private val context: Context){
        private val blockInfo: ExtraTargetContentHolder = ExtraTargetContentHolder()

        fun setTargetView(view: View): Builder {
            blockInfo.targetView = view
            return this
        }

        fun setWidth(width: Int): Builder {
            blockInfo.width = width
            return this
        }

        fun setHeight(height: Int): Builder {
            blockInfo.height = height
            return this
        }

        fun setBackgroundResource(resourceId: Int): Builder {
            blockInfo.backgroundResourceId = resourceId
            return this
        }

        fun setPadding(left: Int, top: Int, right: Int, bottom: Int): Builder {
            blockInfo.leftPadding = left
            blockInfo.topPadding = top
            blockInfo.rightPadding = right
            blockInfo.bottomPadding = bottom
            return this
        }

        fun setPaddingByResourcesId(
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ): Builder {
            blockInfo.leftPadding = context.resources.getDimensionPixelSize(left)
            blockInfo.topPadding = context.resources.getDimensionPixelSize(top)
            blockInfo.rightPadding = context.resources.getDimensionPixelSize(right)
            blockInfo.bottomPadding = context.resources.getDimensionPixelSize(bottom)
            return this
        }

        fun build(): ExtraTargetContentHolder {
            return blockInfo
        }
    }
}