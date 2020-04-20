package com.example.lab1.Filter

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build

class WindowedFilter_alpha : WindowedFIlter() {
    @TargetApi(Build.VERSION_CODES.O)
    override fun editColor(col: Int, args: Array<Any>?): Int {
        val rez = super.editColor(col, args)

        return Color.argb(255 - Color.red(rez), Color.red(rez), Color.red(rez), Color.red(rez))
    }
}