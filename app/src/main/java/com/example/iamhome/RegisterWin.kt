package com.example.iamhome

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class RegisterWin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_win)
    }

    fun readData(view: View)
    {
        val fieldName = findViewById<EditText>(R.id.textName);
        val fieldPassword = findViewById<EditText>(R.id.textPassword);
        val fieldCheckPassword = findViewById<EditText>(R.id.textPassword2);
        val fieldEmail = findViewById<EditText>(R.id.textEmail);

        val userName = fieldName.text;
        val userPassword = fieldPassword.text;
        val userCheckPassword = fieldCheckPassword.text;
        val userEmail = fieldEmail.text;
    }
}