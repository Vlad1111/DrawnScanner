package com.example.lab1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_scrip_list.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import com.example.lab1.Scripts.ScriptRepository


const val EXTRA_SCRIPT_NAME = "com.example.myfirstapp.EXTRA_SCRIPT_NAME"

class ScripListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrip_list)
        fab.setOnClickListener { view ->
            val intent = Intent(this, EditScriptActivity::class.java).apply {
                //putExtra(EXTRA_SCRIPT_NAME, null)
            }
            startActivity(intent)
        }

        val repo = ScriptRepository.getInstance(this)
        val list = repo.all
        var array = list.map { it.name }


        val adapter = ArrayAdapter(this, R.layout.script_listview_item, array)
        scripstListVew.adapter = adapter

        scripstListVew.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItem(position)
            val intent = Intent(this, EditScriptActivity::class.java).apply {
                putExtra(EXTRA_SCRIPT_NAME, element)
            }
            startActivityForResult(intent, 0)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val repo = ScriptRepository.getInstance(this)
        val list = repo.all
        var array = list.map { it.name }


        val adapter = ArrayAdapter(this, R.layout.script_listview_item, array)
        scripstListVew.adapter = adapter
    }
}
