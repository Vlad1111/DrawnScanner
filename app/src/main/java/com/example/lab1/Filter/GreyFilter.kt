package com.example.lab1.Filter

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.graphics.get
import androidx.core.graphics.set


class GreyFilter : BasicFilter() {
    @TargetApi(Build.VERSION_CODES.O)
    override fun editColor(col: Int, args: Array<Any>?): Int {
        var gray: Float =
            (Color.red(col) * 212f + Color.green(col) * 715f + Color.blue(col) * 73f) / 1000f

        return Color.argb(255, gray.toInt(), gray.toInt(), gray.toInt())
    }
}