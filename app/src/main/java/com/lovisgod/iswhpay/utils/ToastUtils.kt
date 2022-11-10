package com.lovisgod.iswhpay.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {

    fun showLong(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}