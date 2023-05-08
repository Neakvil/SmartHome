package com.example.iamhome

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

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
        val fieldName = findViewById<TextView>(R.id.textNameLogIn)
        val fieldPassword = findViewById<TextView>(R.id.textNameLogIn)

        val userName = fieldName.text
        val userPassword = fieldPassword.text

        if(userName.toString().isNotEmpty() && userPassword.toString().isNotEmpty())
        {
            val requestBody = FormBody.Builder()
                .add("username", userName.toString())
                .add("password", userPassword.toString())
                .build()

            val request = Request.Builder()
                .url("http://yourserver.com/api/login")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Обробка помилок
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val json = JSONObject(body)

                    if (json.has("token")) {
                        token = json.getString("token")
                        // Збереження токену на пристрої

                        // Відкриття іншої активності або виконання іншого коду, що потребує авторизації
                    } else {
                        // Обробка помилок
                    }
                }
            })
        } else {
            // Повідомлення про неправильні дані
        }
        }
    }
    


