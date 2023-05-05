package com.example.iamhome

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.red
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

    //Відкритя форми входу в акаунт
    fun openLogIn(view: View)
    {
        val randomIntent = Intent(this, Log_in::class.java)

        startActivity(randomIntent)
    }

    // Считування даних з полей форми
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

        if(fieldName.text.toString().isNotEmpty() && fieldPassword.text.toString().isNotEmpty() && fieldCheckPassword.text.toString().isNotEmpty() && fieldEmail.text.toString().isNotEmpty())
        {
            if(userPassword.toString() != userCheckPassword.toString())
            {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Error")
                builder.setMessage("Your password does not match")

                val dialog = builder.create()
                dialog.show()
            }
        }
        else
        {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Error")
            builder.setMessage("Fill in all the fields")

            val dialog = builder.create()
            dialog.show()
        }


    }
}