package com.example.lab1

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.lab1.Scripts.Script
import com.example.lab1.Scripts.ScriptRepository
import kotlinx.android.synthetic.main.activity_edit_script.*

class EditScriptActivity : AppCompatActivity() {
    var scriptName: String? = null
    var script: Script? = Script()
    var selectedTabbar = 1
    val repo = ScriptRepository.getInstance(this)

    private class TabListener : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
        var editScriptActivity: EditScriptActivity? = null

        constructor(editScriptActivity: EditScriptActivity?) {
            this.editScriptActivity = editScriptActivity
        }


        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            editScriptActivity!!.switchTabsTo(p0!!.text.toString())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_script)
        scriptName = intent.getStringExtra(EXTRA_SCRIPT_NAME)
        if(scriptName == null){
            scriptName = ""
            script!!.name = scriptName
        }
        else{
            script = repo.getScript(scriptName)
            if(script == null){
                scriptName = ""
                script = Script()
                script!!.name = scriptName
            }
        }
        edit_script_extra_TextField.setText(scriptName)
        script_Edit_TextField.setText(script!!.color)
        script_Edit_TextField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(selectedTabbar == 1)
                    script!!.color = p0.toString()
            }
        })
        script_tabBar.addOnTabSelectedListener(TabListener(this))

        edit_save_script_button.setOnClickListener {
            script!!.name = edit_script_extra_TextField.text.toString()
            repo.addScript(script, this)
            setResult(Activity.RESULT_OK)
            finish()
        }
        edit_delete_script_button.setOnClickListener {
            script!!.name = edit_script_extra_TextField.text.toString()
            repo.deleteScript(script, this)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    fun switchTabsTo(name:String)
    {
        if(name == "Image" || name == "image" || name == "IMAGE"){
            selectedTabbar = 2
            //script_Edit_TextField.setText(script.image)
            script_Edit_TextField.setText(script!!.colorBlockInstructions)
        }
        else{
            selectedTabbar = 1
            script_Edit_TextField.setText(script!!.color)
        }
    }

}