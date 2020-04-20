package com.example.lab1.Filter

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import kotlin.math.log2

class ContrastCorrectionFilter : BasicFilter() {
    @TargetApi(Build.VERSION_CODES.O)
    override fun editColor(col: Int, args: Array<Any>?): Int {
        val r = log2(1.toDouble() + Color.red(col)) * 255 / log2(256.toDouble())
        val g = log2(1.toDouble() + Color.green(col)) * 255 / log2(256.toDouble())
        val b =log2(1.toDouble() + Color.blue(col)) * 255 / log2(256.toDouble())


        return Color.argb(255, r.toInt(), g.toInt(), b.toInt())
    }
}