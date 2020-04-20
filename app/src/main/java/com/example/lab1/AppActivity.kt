package com.example.lab1

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.get
import androidx.core.graphics.set
import com.example.lab1.Filter.*
import com.example.lab1.Scripts.ScriptRepository
import kotlinx.android.synthetic.main.activity_chropping.*
import kotlinx.android.synthetic.main.app_activity_layout.*
import yuku.ambilwarna.AmbilWarnaDialog
import java.io.*
import java.lang.Exception

const val CAMERA_IMAGE = "CAMERA_IMAGE";

var _image_bm: Bitmap? = null
var _image_bm_low_quality: Bitmap? = null

class AppActivity : AppCompatActivity() {

    private val IMAGE_CAPTURE_CODE = 100
    private val IMAGE_PICK_CODE = 101

    private val PERMISION_CODE_CAMERA = 1000
    private val PERMISION_CODE_STORAGE = 2000


    private var _image_uri: Uri? = null
    //private var _image_bm: Bitmap? = null
    private var _image_bm_edited: Bitmap? = null
    private var lastEditingFilter: IFilter = BasicFilter()
    private var lastEditingArguments: Array<Any> = arrayOf()

    private var USE_LOW_QWALITY: Boolean = false
    private val repo = ScriptRepository.getInstance(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_layout)

        /*val message = intent.getStringExtra(EXTRA_MESSAGE)
        findViewById<TextView>(R.id.textFieldUsername).apply {
            text = message
        }*/


        button_capture.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                ) {
                    //FUCK ME
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, PERMISION_CODE_CAMERA)
                } else {
                    //FUCK YEAH
                    openCamera()
                }
            else {
                //FUCK THIS
                openCamera()
            }
        }
        camera_preview.setOnClickListener {
            if(_image_bm != null){
                val intent = Intent(this, ChroppingActivity::class.java)
                startActivity(intent)
            }
            else Toast.makeText(this, "Take a photo first", Toast.LENGTH_SHORT).show()
        }
        switch_use_low_quality.setOnClickListener { USE_LOW_QWALITY = !USE_LOW_QWALITY }

        button_save_capture.setOnClickListener { saveImageEdited() }

        button_load_image.setOnClickListener {
            //Intent to pick image
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        color1_button.setOnClickListener {
            val colorPicker = AmbilWarnaDialog(this,
                (color1_button.background as ColorDrawable).color,
                object: AmbilWarnaDialog.OnAmbilWarnaListener{
                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        color1_button.setBackgroundColor(color)
                    }

                })
            colorPicker.show()
        }
        color2_button.setOnClickListener {
            val colorPicker = AmbilWarnaDialog(this,
                (color2_button.background as ColorDrawable).color,
                object: AmbilWarnaDialog.OnAmbilWarnaListener{
                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        color2_button.setBackgroundColor(color)
                    }

                })
            colorPicker.show()
        }

        button_edit_reset.setOnClickListener {
            lastEditingFilter = BasicFilter()
            _image_bm_edited = _image_bm_low_quality
            camera_preview.setImageBitmap(_image_bm_edited)
        }
        button_edit_gray.setOnClickListener {
            lastEditingFilter = GreyFilter()
            lastEditingArguments = arrayOf()
            _image_bm_edited = lastEditingFilter.edit(_image_bm_low_quality, null)
            camera_preview.setImageBitmap(_image_bm_edited)
        }
        button_edit_bw.setOnClickListener {
            lastEditingFilter = BlackAndWhiteFilter()
            lastEditingArguments = arrayOf((color1_button.background as ColorDrawable).color, (color2_button.background as ColorDrawable).color)
            _image_bm_edited = (lastEditingFilter as BlackAndWhiteFilter).edit(_image_bm_low_quality, lastEditingArguments)
            camera_preview.setImageBitmap(_image_bm_edited)
        }
        button_edit_bwa.setOnClickListener {
            lastEditingFilter = BlackAndWhiteFilter_alpha()
            lastEditingArguments = arrayOf((color1_button.background as ColorDrawable).color, (color2_button.background as ColorDrawable).color)
            _image_bm_edited = (lastEditingFilter as BlackAndWhiteFilter_alpha).edit(_image_bm_low_quality, lastEditingArguments)
            camera_preview.setImageBitmap(_image_bm_edited)
        }
        button_edit_windowed.setOnClickListener {
            lastEditingFilter = WindowedFIlter()
            lastEditingArguments = arrayOf((color1_button.background as ColorDrawable).color, (color2_button.background as ColorDrawable).color)
            _image_bm_edited = (lastEditingFilter as WindowedFIlter).edit(_image_bm_low_quality, lastEditingArguments)
            camera_preview.setImageBitmap(_image_bm_edited)
        }
        button_edit_windowed_a.setOnClickListener {
            lastEditingFilter = WindowedFilter_alpha()
            lastEditingArguments = arrayOf((color1_button.background as ColorDrawable).color, (color2_button.background as ColorDrawable).color)
            _image_bm_edited = (lastEditingFilter as WindowedFilter_alpha).edit(_image_bm_low_quality, lastEditingArguments)
            camera_preview.setImageBitmap(_image_bm_edited)
        }
        button_edit_windowed_color.setOnClickListener {
            lastEditingFilter = WindowedCollorFilter()
            lastEditingArguments = arrayOf((color1_button.background as ColorDrawable).color, (color2_button.background as ColorDrawable).color)
            _image_bm_edited = (lastEditingFilter as WindowedCollorFilter).edit(_image_bm_low_quality, lastEditingArguments)
            camera_preview.setImageBitmap(_image_bm_edited)
        }
        button_edit_windowed_color_a.setOnClickListener {
            lastEditingFilter = WindowedCollorFilter_alpha()
            lastEditingArguments = arrayOf((color1_button.background as ColorDrawable).color, (color2_button.background as ColorDrawable).color)
            _image_bm_edited = (lastEditingFilter as WindowedCollorFilter_alpha).edit(_image_bm_low_quality, lastEditingArguments)
            camera_preview.setImageBitmap(_image_bm_edited)
        }
        button_edit_contrast_corection.setOnClickListener {
            lastEditingFilter = ContrastCorrectionFilter()
            lastEditingArguments = arrayOf((color1_button.background as ColorDrawable).color, (color2_button.background as ColorDrawable).color)
            _image_bm_edited = (lastEditingFilter as ContrastCorrectionFilter).edit(_image_bm_low_quality, lastEditingArguments)
            camera_preview.setImageBitmap(_image_bm_edited)
        }



        val repo = ScriptRepository.getInstance(this)
        val list = repo.all
        val array = list.map { it.name }


        val adapter = ArrayAdapter(this, R.layout.script_listview_item, array)
        select_script_filter_spinner.adapter = adapter
        select_script_filter_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val script = list[position]
                lastEditingFilter = ScriptedFilter()
                lastEditingArguments = arrayOf((color1_button.background as ColorDrawable).color, (color2_button.background as ColorDrawable).color, script)
                if(_image_bm_low_quality != null){
                    _image_bm_edited = (lastEditingFilter as ScriptedFilter).edit(_image_bm_low_quality, lastEditingArguments)
                    camera_preview.setImageBitmap(_image_bm_edited)
                }
            }
        }
    }

    private fun openCamera() {
        if(!USE_LOW_QWALITY){
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")

            _image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(!USE_LOW_QWALITY)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, _image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    @SuppressLint("MissingSuperCall")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == IMAGE_CAPTURE_CODE){
                //camera_preview.setImageURI(_image_uri)

                if (_image_uri != null) {
                    /**val tn = ImageDecoder.createSource(contentResolver, _image_uri!!)
                    _image_bm = ImageDecoder.decodeBitmap(tn)
                    camera_preview.setImageBitmap(_image_bm)*/

                    /*val tn = ImageDecoder.createSource(contentResolver, _image_uri!!)
                    val bm = ImageDecoder.decodeDrawable(tn)
                    _image_bm = (bm as BitmapDrawable).bitmap
                    camera_preview.setImageBitmap(_image_bm)*/

                    if (!USE_LOW_QWALITY) {
                        _image_bm = MediaStore.Images.Media.getBitmap(contentResolver, _image_uri)
                        _image_bm_low_quality = makeACopyOf_image_bm()
                        camera_preview.setImageBitmap(_image_bm)
                    }
                }
                if(USE_LOW_QWALITY) {
                    if (data != null) {
                        _image_bm = data.extras!!.get("data") as Bitmap
                        _image_bm_low_quality = makeACopyOf_image_bm()
                        camera_preview.setImageBitmap(_image_bm)
                    }
                }
            }
            else if(requestCode == IMAGE_PICK_CODE){
                if (data != null) {
                    _image_bm = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
                    _image_bm_low_quality = makeACopyOf_image_bm()
                    camera_preview.setImageBitmap(_image_bm)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISION_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    //FUCK OFF
                    Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISION_CODE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(_image_bm_edited != null)
                        saveImageToExternalStorage(_image_bm_edited!!, "image")
                } else {
                    //FUCK OFF
                    Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createCustomFile( fileName:String)
    {
        try {
            var path =
                File(applicationContext.getFilesDir(), "MyAppName" + File.separator + "Images");
            if (!path.exists()) {
                path.mkdirs();
            }
            var outFile = File(path, fileName + ".jpeg");
            //now we can create FileOutputStream and write something to file
        } catch (e:FileNotFoundException) {
            //Log.e(TAG, "Saving received message failed with", e);
        } catch ( e:IOException) {
            //Log.e(TAG, "Saving received message failed with", e);
        }
    }

    private fun saveImageToExternalStorage(bit:Bitmap, picName:String){
        val filePath = Environment.getExternalStorageDirectory().absolutePath
        val dir = File(filePath + "/Schan/")
        dir.mkdir()

        val file = File(dir, picName + ".png")

        try{
            val outputStream = FileOutputStream(file)
            bit.compress(Bitmap.CompressFormat.PNG, 1, outputStream)
            outputStream.flush()
            outputStream.close()
            //Snackbar.make(view, "Image Saved", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            Toast.makeText(this, "Image saved to " + filePath, Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            //Snackbar.make(view, e.message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageEdited() {
        if(_image_bm_edited != null){
            _image_bm_edited = lastEditingFilter.edit(_image_bm, lastEditingArguments)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                ) {
                    //FUCK ME
                    val permission = arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, PERMISION_CODE_STORAGE)
                } else {
                    //FUCK YEAH
                    saveImageToExternalStorage(_image_bm_edited!!, "image")
                }
            else {
                //FUCK THIS
                saveImageToExternalStorage(_image_bm_edited!!, "image")
            }
        }
    }

    private fun makeACopyOf_image_bm (): Bitmap?{
        if(_image_bm == null)
            return null


        var width = _image_bm!!.width
        var height =  _image_bm!!.height
        val trashHold = 250f

        if(width > trashHold){
            val ratio = trashHold / width
            height = (height * ratio).toInt()
            width = trashHold.toInt()
        }
        if(height > trashHold){
            val ratio = trashHold / height
            width = (width * ratio).toInt()
            height = trashHold.toInt()
        }

        val bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bm.setHasAlpha(true)

        for(i in 0 until bm.width)
            for(j in 0 until bm.height) {
                val x = ((1f * i / bm.width) * _image_bm!!.width).toInt()
                val y = ((1f * j / bm.height) * _image_bm!!.height).toInt()

                val col = _image_bm!![x,y]
                bm[i,j] = col
            }
        return bm
    }
}
