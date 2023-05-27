package com.example.iamhome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.iamhome.qrscanner.QRScannerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openScanner(view: View)
    {
        //val randomIntent = Intent(this, QRScannerActivity::class.java)
        val randomIntent = Intent(this, Confirmation_Form::class.java)

        startActivity(randomIntent)
    }

    fun openRegister(view: View)
    {
        val randomIntent = Intent(this, RegisterWin::class.java)
        val randomIntentLogIn = Intent(this, Log_in::class.java)

        val sharedPreferences = getSharedPreferences("SaveUserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        val fieldEmail = findViewById<TextView>(R.id.textEmailLogIn)

        if(email.toString().isNotEmpty()) startActivity(randomIntentLogIn)
        else startActivity(randomIntent)
    }
}