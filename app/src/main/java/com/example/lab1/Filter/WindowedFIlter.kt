package com.example.lab1.Filter

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import kotlin.math.PI
import kotlin.math.atan

open class WindowedFIlter : BasicFilter() {
    @TargetApi(Build.VERSION_CODES.O)
    override fun editColor(col: Int, args: Array<Any>?): Int {
        var gray: Float =
            (Color.red(col) * 212 + Color.green(col) * 715 + Color.blue(col) * 73) / 1000f
        val auxColor1 = args?.get(0) as Int
        var auxGray1 : Float = (Color.red(auxColor1) * 212f + Color.green(auxColor1) * 715f + Color.blue(auxColor1) * 73f) / 1000f
        val auxColor2 = args.get(1) as Int
        var auxGray2 : Float = (Color.red(auxColor2) * 212f + Color.green(auxColor2) * 715f + Color.blue(auxColor2) * 73f) / 1000f

        if(auxGray1 > auxGray2){
            val aux = auxGray1
            auxGray1 = auxGray2
            auxGray2 = aux
        }

        if(gray <= auxGray1)
            return Color.BLACK
        else if(gray >= auxGray2)
            return Color.WHITE

        gray = 255 * (gray - auxGray1) / (auxGray2 - auxGray1)

        return Color.argb( 255, gray.toInt(), gray.toInt(), gray.toInt())
    }
}