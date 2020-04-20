package com.example.lab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.lab1.Scripts.ScriptRepository
import kotlinx.android.synthetic.main.activity_main.*


const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.mainMenuEditButton)

        ScriptRepository.getInstance(this)

        loginButton.setOnClickListener {
            val intent = Intent(this, AppActivity::class.java).apply {
                //putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)

            /*Log.v(TAG, "login...");
            GlobalScope.launch {
                var aux = AuthRepository.login(username.text.toString(), password.text.toString())

                username.text = aux.toString()
            }*/
        }
        mainMenuScriptButton.setOnClickListener {
            val intent = Intent(this, ScripListActivity::class.java)
            startActivity(intent)
        }
    }
}
