package com.example.lab1

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_chropping.*
import android.widget.ArrayAdapter
import androidx.core.graphics.get
import androidx.core.graphics.set
import java.lang.Exception
import kotlin.math.sqrt


class ChroppingActivity : AppCompatActivity() {

    private var originalImage: Bitmap? = null
    private var points: Array<Pair<Float, Float>> = Array(4, init = { return@Array Pair(0f, 0f) })
    private var isShowingOriginal: Boolean = true
    private var chroppedImage: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chropping)

        val languages = resources.getStringArray(R.array.Resolutions)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, languages)
        chropping_ratio_spinner.adapter = adapter
        chropping_ratio_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                chropping_resolution_textfield.setText(languages[position])
                if (!isShowingOriginal) {
                    chroppedImage = creteNewBitmap(_image_bm)
                    chropingImagePreview.setImageBitmap(chroppedImage)
                }
            }
        }
        chropping_resolution_textfield.setText(languages[0])

        chropingImagePreview.setImageBitmap(_image_bm_low_quality)
        if (originalImage != null) {
        }
        points[0] = Pair<Float, Float>(0f, 0f)
        points[1] = Pair<Float, Float>(0f, 1f)
        points[2] = Pair<Float, Float>(1f, 1f)
        points[3] = Pair<Float, Float>(1f, 0f)

        show_croped_button.setOnClickListener {
            if (isShowingOriginal) {
                chroppedImage = creteNewBitmap(_image_bm_low_quality)
                chropingImagePreview.setImageBitmap(chroppedImage)
            } else {
                chropingImagePreview.setImageBitmap(_image_bm_low_quality)
                drowContur()
            }
            isShowingOriginal = !isShowingOriginal
        }
        applyChroppingButton.setOnClickListener {
            if (chroppedImage == null)
                chroppedImage = creteNewBitmap(_image_bm_low_quality)
            _image_bm = creteNewBitmap(_image_bm)
            _image_bm_low_quality = chroppedImage
            finish()
        }

        drowContur()
    }

    //var BitmapSize = 40
    private fun drowContur() {
        if (_image_bm_low_quality == null)
            return


        val bitmapiamge = Bitmap.createBitmap(_image_bm_low_quality!!)

        val canvas = Canvas(bitmapiamge)
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GREEN
        paint.strokeWidth = _image_bm_low_quality!!.width / 200f
        var i = 0
        while (i < points.size) {
            var j = i + 1
            if (j >= points.size)
                j = 0
            canvas.drawLine(
                points[i].first * _image_bm_low_quality!!.width, points[i].second * _image_bm_low_quality!!.height,
                points[j].first * _image_bm_low_quality!!.width, points[j].second * _image_bm_low_quality!!.height, paint
            )
            i++
        }
        var crodx =
            (_image_bm_low_quality!!.width * (points[0].first + points[1].first + points[2].first + points[3].first) / 4).toInt()
        var crody =
            (_image_bm_low_quality!!.height * (points[0].second + points[1].second + points[2].second + points[3].second) / 4).toInt()
        canvas.drawRect(Rect(crodx - 1, crody - 1, crodx + 1, crody + 1), paint)
        chropingImagePreview?.setImageBitmap(bitmapiamge);
    }

    private fun displayZoom(X: Float, Y: Float) {
        var x: Int = (X * _image_bm!!.width).toInt()
        var y: Int = (Y * _image_bm!!.height).toInt()

        var zoomWidth = _image_bm!!.width / 10
        var zoomHeight = _image_bm!!.height / 20

        if (zoomHeight > 80)
            zoomHeight = 80
        if (zoomWidth > 150)
            zoomHeight = 150

        var bm = Bitmap.createBitmap(zoomWidth * 2 + 1, zoomHeight * 2 + 1, Bitmap.Config.ARGB_8888)

        for (i in -zoomWidth..zoomWidth)
            for (j in -zoomHeight..zoomHeight) {
                var xx = x + i
                var yy = y + j
                var color = Color.BLACK
                if (xx >= 0 && yy >= 0)
                    if (xx < _image_bm!!.width && yy < _image_bm!!.height)
                        color = _image_bm!!.get(xx, yy)
                bm.set(i + zoomWidth, j + zoomHeight, color)
            }
        val canvas = Canvas(bm)
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GREEN
        paint.strokeWidth = 1f
        canvas.drawRect(Rect(zoomWidth - 1, zoomHeight - 1, zoomWidth + 1, zoomHeight + 1), paint)

        chropedImageZoomPreview.setImageBitmap(bm)
    }


    private fun getDistancePoints(A: Pair<Float, Float>, B: Pair<Float, Float>): Float {
        return (A.first - B.first) * (A.first - B.first) +
                (A.second - B.second) * (A.second - B.second)
    }

    private fun onPressPoint(x: Float, y: Float) {
        chroppedImage = null
        isShowingOriginal = true
        val location = IntArray(2)
        chropingImagePreview.getLocationOnScreen(location)
        val X = (x - location[0]) / chropingImagePreview.width
        val Y = (y - location[1]) / chropingImagePreview.height

        if (X < 0 || Y < 0 || X >= 1 || Y >= 1)
            return

        var min = 0
        var i = 1
        while (i < points.size) {
            if (getDistancePoints(points[i], Pair(X, Y)) < getDistancePoints(
                    points[min],
                    Pair(X, Y)
                )
            )
                min = i
            i++
        }
        points[min] = Pair(X, Y)


        displayZoom(X, Y)
        drowContur()

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                onPressPoint(event.rawX, event.rawY)
            }
            MotionEvent.ACTION_MOVE -> {
                onPressPoint(event.rawX, event.rawY)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
            }
        }


        return true
    }

    private fun cetIntermidiateColor(
        c1: Int,
        c2: Int,
        c3: Int,
        c4: Int,
        vx: Float,
        vy: Float
    ): Int {
        var r = (
                Color.red(c1) * (1 - vx) * (1 - vy) +
                        Color.red(c2) * vx * (1 - vy) +
                        Color.red(c3) * vx * vy +
                        Color.red(c4) * (1 - vx) * vy
                ) / 1
        var g = (
                Color.green(c1) * (1 - vx) * (1 - vy) +
                        Color.green(c2) * vx * (1 - vy) +
                        Color.green(c3) * vx * vy +
                        Color.green(c4) * (1 - vx) * vy
                ) / 1
        var b = (
                Color.blue(c1) * (1 - vx) * (1 - vy) +
                        Color.blue(c2) * vx * (1 - vy) +
                        Color.blue(c3) * vx * vy +
                        Color.blue(c4) * (1 - vx) * vy
                ) / 1
        return Color.argb(255, r.toInt(), g.toInt(), b.toInt())
    }

    private fun creteNewBitmap(imageBm: Bitmap?): Bitmap {
        var text_res:String = chropping_resolution_textfield.text.toString()
        var findRez = text_res.split("x")
        if(findRez.size != 2){
            Toast.makeText(this, "The resolution is not corecctly written " + findRez.size, Toast.LENGTH_SHORT).show()
            return Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888)
        }
        var width_sze = imageBm!!.width
        var height_size = imageBm!!.height

        try {
            if(findRez[0][0] == '['){
                var rez_x = Integer.parseInt(findRez[0].removePrefix("["))
                var rez_y = Integer.parseInt(findRez[1].removeSuffix("]"))
                var rez = 1f * rez_x / rez_y
                var ccc = width_sze * width_sze + height_size * height_size
                width_sze = (rez * sqrt((ccc / (rez * rez + 1)).toDouble())).toInt()
                height_size = (sqrt((ccc / (rez * rez + 1)).toDouble())).toInt()
            }
            else{
                width_sze = Integer.parseInt(findRez[0])
                height_size = Integer.parseInt(findRez[1])
            }
        }catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            return Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888)
        }

        var bm = Bitmap.createBitmap(width_sze, height_size, Bitmap.Config.ARGB_8888)
        for (i in 0 until width_sze)
            for (j in 0 until height_size) {
                var vi = 1f * i / width_sze
                var vj = 1f * j / height_size

                var x1 = points[0].first + (points[3].first - points[0].first) * vi
                var y1 = points[0].second + (points[3].second - points[0].second) * vi
                var x2 = points[1].first + (points[2].first - points[1].first) * vi
                var y2 = points[1].second + (points[2].second - points[1].second) * vi

                var x3 = points[0].first + (points[1].first - points[0].first) * vj
                var y3 = points[0].second + (points[1].second - points[0].second) * vj
                var x4 = points[3].first + (points[2].first - points[3].first) * vj
                var y4 = points[3].second + (points[2].second - points[3].second) * vj

                var Meg = (y2 - y1) / (x2 - x1)
                var Mhf = (y4 - y3) / (x4 - x3)

                var Ceg = y1 - Meg * x1
                var Chf = y3 - Mhf * x3

                var x = (Chf - Ceg) / (Meg - Mhf)
                var y = Meg * x + Ceg


                x *= imageBm.width
                y *= imageBm.height


                //var x = (points[3].first - points[0].first) * i + points[0].first * bm.width
                //var y = (points[1].second - points[0].second) * j + points[0].second * bm.height


                if (x < 0 && y < 0) {
                    bm.set(i, j, Color.BLACK)
                } else if (x >= 0) {
                    if (y >= 0) {
                        if (x < imageBm!!.width - 1 && y < imageBm!!.height - 1) {
                            var c1 = imageBm!!.get(x.toInt(), y.toInt())
                            var c2 = imageBm!!.get(x.toInt() + 1, y.toInt())
                            var c3 = imageBm!!.get(x.toInt() + 1, y.toInt() + 1)
                            var c4 = imageBm!!.get(x.toInt(), y.toInt() + 1)

                            var vx = x - x.toInt()
                            var vy = y - y.toInt()

                            var col = cetIntermidiateColor(c1, c2, c3, c4, vx, vy)
                            bm.set(i, j, col)
                        } else if (x.toInt() == imageBm!!.width - 1 || y.toInt() == imageBm!!.height - 1) {
                            bm.set(i, j, imageBm!!.get(x.toInt(), y.toInt()))
                        } else bm.set(i, j, Color.RED)
                    } else bm.set(i, j, Color.BLUE)
                } else bm.set(i, j, Color.CYAN)
            }
        return bm
    }
}