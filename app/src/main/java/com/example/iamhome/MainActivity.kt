package com.example.iamhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openRegister(view: View)
    {
        val randomIntent = Intent(this, RegisterWin::class.java)

        startActivity(randomIntent)
    }
}