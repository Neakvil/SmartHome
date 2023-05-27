package com.example.iamhome

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Confirmation_Form : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation_form)

        val email = intent.getStringExtra("email")
        val userEmail = findViewById<TextView>(R.id.userEmailTextView)

        userEmail.text = email.toString()
        val openGmailButton: Button = findViewById(R.id.openGmailButton)
        openGmailButton.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage("com.google.android.gm")
            startActivity(intent)
        }
    }
}