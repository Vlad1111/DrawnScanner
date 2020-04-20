package com.example.lab1.Filter

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build

class WindowedCollorFilter : WindowedFIlter() {
    @TargetApi(Build.VERSION_CODES.O)
    override fun editColor(col: Int, args: Array<Any>?): Int {
        val rez = super.editColor(col, args)

        var r= (Color.red(col) * (2 * Color.red(rez)) / 255f).toInt()
        var g= (Color.green(col) * (2 * Color.red(rez)) / 255f).toInt()
        var b= (Color.blue(col) * (2 * Color.red(rez)) / 255f).toInt()

        if(r > 255)
            r = 255
        if(g > 255)
            g = 255
        if(b > 255)
            b = 255

        return Color.argb(255, r, g, b)
    }
}
