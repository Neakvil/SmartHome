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

    fun openMain(view: View)
    {
        val randomIntent = Intent(this, HomePage::class.java)

        startActivity(randomIntent)
    }

    fun openRegister(view: View)
    {
        val randomIntent = Intent(this, Log_in::class.java)

        startActivity(randomIntent)
    }
}