package com.example.iamhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class Log_in : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }

    fun readData(view: View)
    {
        val fieldName = findViewById<TextView>(R.id.textNameLogIn)
        val fieldPassword = findViewById<TextView>(R.id.textNameLogIn)

        val userName = fieldName.text
        val userPassword = fieldPassword.text
    }
    
    //Відкритя форми реєстрації
    fun openRegister(view: View)
    {
        val randomIntent = Intent(this, RegisterWin::class.java)

        startActivity(randomIntent)
    }
}