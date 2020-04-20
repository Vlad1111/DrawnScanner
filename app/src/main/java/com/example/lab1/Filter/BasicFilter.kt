package com.example.lab1.Filter

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.graphics.get
import androidx.core.graphics.set

open class BasicFilter : IFilter{
    @TargetApi(Build.VERSION_CODES.O)
    override fun editColor(col: Int, args: Array<Any>?): Int {
        return col
    }

    override fun edit(image: Bitmap?, args: Array<Any>?): Bitmap? {
        val newB = Bitmap.createBitmap(image!!.width, image.height, Bitmap.Config.ARGB_8888)

        for(i in 0 until newB.width)
            for(j in 0 until newB.height) {
                newB[i,j] = editColor(image[i, j], args)
            }
        return newB
    }
}