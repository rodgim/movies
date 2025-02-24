package com.rodgim.movies.ui.discovery.hintcase.utils

import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.graphics.Point
import android.os.Build
import android.util.Size
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager


object DimenUtils {
    fun getStatusBarHeight(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = activity.window.decorView.rootWindowInsets
            insets?.getInsets(WindowInsets.Type.statusBars())?.top ?: 0
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.rootWindowInsets?.systemWindowInsetTop ?: 0
        } else {
            var statusBarHeight = 0
            val id = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (id > 0) {
                statusBarHeight = activity.resources.getDimensionPixelSize(id)
            }
            return statusBarHeight
        }
    }

    fun getActionBarHeight(context: Context): Int {
        var actionBarHeight = 0
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return actionBarHeight
    }

    fun dipToPixels(context: Context, valueInDP: Float): Int {
        val displayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDP, displayMetrics).toInt()
    }

    fun getNavigationBarSizeIfExistAtTheBottom(context: Context): Point {
        val appUsableSize = getAppUsableScreenSize(context)
        val realScreenSize = getRealScreenSize(context)
        val navigationPoint: Point
        val navigationBarIsPresent = appUsableSize.y < realScreenSize.y
        navigationPoint = if (navigationBarIsPresent) {
            Point(appUsableSize.x, realScreenSize.y - appUsableSize.y)
        } else {
            Point()
        }
        return navigationPoint
    }

    fun getNavigationBarSizeIfExistOnTheRight(context: Context): Point {
        val appUsableSize = getAppUsableScreenSize(context)
        val realScreenSize = getRealScreenSize(context)
        val navigationPoint: Point
        val navigationBarIsPresent = appUsableSize.x < realScreenSize.x
        if (navigationBarIsPresent) {
            navigationPoint = Point(realScreenSize.x - appUsableSize.x, appUsableSize.y)
        } else {
            navigationPoint = Point()
        }
        return navigationPoint
    }

    fun getAppUsableScreenSize(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics

            val windowInsets = windowMetrics.windowInsets
            val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars()
                        or WindowInsets.Type.displayCutout()
            )

            val insetsWidth: Int = insets.right + insets.left
            val insetsHeight: Int = insets.top + insets.bottom

            val bounds = windowMetrics.bounds
            val legacySize: Size = Size(
                bounds.width() - insetsWidth,
                bounds.height() - insetsHeight
            )
            size.x = legacySize.width
            size.y = legacySize.height
        } else {
            val display = windowManager.defaultDisplay
            display.getSize(size)
        }
        return size
    }

    fun getRealScreenSize(context: Context): Point {
        val size = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val windowMetrics = windowManager.currentWindowMetrics
            val bounds = windowMetrics.bounds
            size.x = bounds.width()
            size.y = bounds.height()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            display.getRealSize(size)
        } else {
            var decorView: View? = null
            if (context is Activity) {
                decorView = context.window.decorView
            } else if (context is ContextThemeWrapper) {
                val baseContext = context.baseContext
                if (baseContext is Activity) {
                    decorView = baseContext.window.decorView
                }
            }
            decorView?.let { dv ->
                size.x = dv.width
                size.y = dv.height
            }
        }
        return size
    }
}
