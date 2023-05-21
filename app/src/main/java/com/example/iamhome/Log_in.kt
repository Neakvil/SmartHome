package com.example.iamhome

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
                        Log.i("LogIn", "Errorsssssss")
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
    


