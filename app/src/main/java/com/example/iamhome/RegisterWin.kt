package com.example.iamhome

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class RegisterWin : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_win)
    }

    fun readData(view: View)
    {
        val fieldName = findViewById<EditText>(R.id.textName)
        val fieldPassword = findViewById<EditText>(R.id.textPassword)
        val fieldCheckPassword = findViewById<EditText>(R.id.textPassword2)
        val fieldEmail = findViewById<EditText>(R.id.textEmail)

        val userName = fieldName.text
        val userPassword = fieldPassword.text
        val userCheckPassword = fieldCheckPassword.text
        val userEmail = fieldEmail.text
    }
}