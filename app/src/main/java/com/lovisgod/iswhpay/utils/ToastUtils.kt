package com.lovisgod.iswhpay.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.widget.ScrollView
import android.widget.Toast

object ToastUtils {

    fun showLong(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }




    /**
     * Print receipt of the transaction*/
    fun getScreenBitMap(activity: Activity, view: ScrollView): Bitmap? {
        var rootview = view

        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        //  this get the width of the layout even beyond the visible screen
        var width = view.getChildAt(0).width

        // this will get the height of the layout even beyond the visible screen
        var height = view.getChildAt(0).height
        // Create a mutable bitmap

        // Create a mutable bitmap
        val secondScreen = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Created a canvas using the bitmap
        val c = Canvas(secondScreen)

        val bgDrawable: Drawable? = view.background
        if (bgDrawable != null) bgDrawable.draw(c) else c.drawColor(Color.WHITE)
        rootview.draw(c)
        return secondScreen
    }

    fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(
            v.width,
            v.height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }
}