package com.example.iamhome

import android.content.Context
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class Log_in : AppCompatActivity() {

    private val client = OkHttpClient()
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val sharedPreferences = getSharedPreferences("SaveUserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        val fieldEmail = findViewById<TextView>(R.id.textEmailLogIn)

        if(email.toString().isNotEmpty())
        {
            fieldEmail.text = email.toString()
        }
    }



    //Відкритя форми реєстрації
    fun openRegister(view: View)
    {
        val randomIntent = Intent(this, RegisterWin::class.java)

        startActivity(randomIntent)
    }

    fun readData(view: View)
    {
        val randomIntent = Intent(this, HomePage::class.java)
        val randomIntentVerif = Intent(this, Confirmation_Form::class.java)

        val fieldEmail = findViewById<TextView>(R.id.textEmailLogIn)
        val fieldPassword = findViewById<TextView>(R.id.textPasswordLogIn)

        val userEmail = fieldEmail.text
        val userPassword = fieldPassword.text
        if(userEmail.toString().isNotEmpty() && userPassword.toString().isNotEmpty())
        {
            Log.i("LogIn", "StartReadData")
            val requestBody = FormBody.Builder()
                .add("email", userEmail.toString())
                .add("password", userPassword.toString())
                .build()
            Log.i("LogIn", "StartFeelQueryInfo")
            Log.i("LogIn", "${userEmail.toString()} ${userPassword.toString()}")
            val request = Request.Builder()
                .url("http://192.168.0.192:8080/api/v1/login")
                .post(requestBody)
                .build()

            Log.i("LogIn", "StartQuery")
            Log.i("LogIn", "$request")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(this@Log_in, "Registration successful", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.i("LogIn", "StartonResponse")
                    val body = response.body?.string()
                    Log.i("LogIn", "Body- $body")
                    val json = JSONObject(body)

                    if (json.has("token")) {
                        token = json.getString("token")
                        Log.i("LogIn", "$token")
                        // Збереження токену на пристрої
                        Log.i("LogIn", "SaveToken")

                        // Відкриття іншої активності або виконання іншого коду, що потребує авторизації
                            randomIntent.putExtra("token", token)
                            randomIntent.putExtra("email", userEmail.toString()) // Assuming you have the email variable
                        startActivity(randomIntent)
                    } else {
                        // Обробка помилок
                        val errorMessage = json.getString("message")
                        Log.i("LogIn", "Error message: $errorMessage")
                        if(errorMessage == "Please verify your email before logging in")
                        {
                                randomIntentVerif.putExtra("email", userEmail.toString()) // Assuming you have the email variable
                            startActivity(randomIntentVerif)
                        }
                        // Використовуйте зміну errorMessage для відображення тексту помилки або подальшої обробки
//                        runOnUiThread {
//                            Toast.makeText(this@Log_in, errorMessage, Toast.LENGTH_SHORT).show()
//                        }
                    }
                }
            })
        } else {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Error")
            builder.setMessage("Your data was entered incorrectly, please check the spelling!")

            val dialog = builder.create()
            dialog.show()
        }
        }
    }


