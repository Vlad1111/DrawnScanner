package com.example.lab1.Filter

import android.graphics.Bitmap
import android.graphics.Color

interface IFilter {
    fun editColor(col: Int, args: Array<Any>?): Int
    fun edit(image: Bitmap?, args: Array<Any>?): Bitmap?
}