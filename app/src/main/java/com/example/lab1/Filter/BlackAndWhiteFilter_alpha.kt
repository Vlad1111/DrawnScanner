package com.example.lab1.Filter

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import kotlin.math.PI
import kotlin.math.atan

class BlackAndWhiteFilter_alpha : BasicFilter() {
    @TargetApi(Build.VERSION_CODES.O)
    override fun editColor(col: Int, args: Array<Any>?): Int {
        var gray: Int =
            (Color.red(col) * 212 + Color.green(col) * 715 + Color.blue(col) * 73) / 1000
        val auxColor = args?.get(0) as Int
        val auxGray : Float = (Color.red(auxColor) * 212f + Color.green(auxColor) * 715f + Color.blue(auxColor) * 73f) / 1000f

        //if(gray < 50f)
        //    gray = 0f

        gray = 255 - gray

        gray = (255f * (0.5f + atan((gray.toDouble() - 255 + auxGray) / 10) / (PI / 2))).toInt()

        gray = 255 - gray
        if(gray < 0)
            gray = 0;
        if(gray > 255)
            gray = 255

        return Color.argb(
            255 - gray,
            0, 0, 0
        )
    }
}