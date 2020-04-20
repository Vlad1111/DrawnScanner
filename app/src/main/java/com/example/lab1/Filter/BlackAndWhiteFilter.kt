package com.example.lab1.Filter

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build


class BlackAndWhiteFilter : BasicFilter() {
    @TargetApi(Build.VERSION_CODES.O)
    override fun editColor(col: Int, args: Array<Any>?): Int {
        var gray: Float = (Color.red(col) * 212f + Color.green(col) * 715f + Color.blue(col) * 73f) / 1000f
        val auxColor = args?.get(0) as Int
        val auxGray : Float = (Color.red(auxColor) * 212f + Color.green(auxColor) * 715f + Color.blue(auxColor) * 73f) / 1000f

        if(gray > auxGray)
            gray = 255f
        else gray = 0f

        return Color.argb(255, gray.toInt(), gray.toInt(), gray.toInt())
    }
}