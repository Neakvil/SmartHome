package com.example.iamhome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iamhome.model.UserRegistrationData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class RegisterWin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register_win)
    }


    //Відкритя форми входу в акаунт
    fun openLogIn(view: View) {
        val randomIntent = Intent(this, Log_in::class.java)

        startActivity(randomIntent)
    }

    // Считування даних з полей форми
    fun readData(view: View) {
        val randomIntent = Intent(this, Confirmation_Form::class.java)

        val fieldName = findViewById<EditText>(R.id.textName)
        val fieldPassword = findViewById<EditText>(R.id.textPassword)
        val fieldCheckPassword = findViewById<EditText>(R.id.textPasswordConfirm)
        val fieldEmail = findViewById<EditText>(R.id.textEmail)

        val userName = fieldName.text.toString()
        val userPassword = fieldPassword.text.toString()
        val userCheckPassword = fieldCheckPassword.text.toString()
        val userEmail = fieldEmail.text.toString()

        if (userName.isNotEmpty() && userPassword.isNotEmpty() && userCheckPassword.isNotEmpty() && userEmail.isNotEmpty()) {
            if (userPassword != userCheckPassword) {
                Toast.makeText(this, "Your password does not match", Toast.LENGTH_SHORT).show()
            } else if (userEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(this, "Your email is wrong!", Toast.LENGTH_SHORT).show()
            } else {
                val userData = UserRegistrationData(userName, userEmail, userPassword)
                val apiService = Retrofit.Builder()
                    .baseUrl("http://192.168.0.192:8080/api/v1/") // Замініть на URL вашого сервера
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
                val call = apiService.registerUser(userData)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            // Успішна реєстрація
                            val sharedPreferences = getSharedPreferences("SaveUserData", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("email", userEmail.toString()) // Зберегти email в кеш-пам'яті
                            editor.putString("name", userName.toString()) // Зберегти name в кеш-пам'яті
                            editor.apply()

                            Toast.makeText(this@RegisterWin, "Registration successful", Toast.LENGTH_SHORT).show()

                            randomIntent.putExtra("email", userEmail.toString()) // Assuming you have the email variable
                            startActivity(randomIntent)
                        } else {
                            // Помилка реєстрації
                            Toast.makeText(this@RegisterWin, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Помилка під час виконання запиту
                        Toast.makeText(this@RegisterWin, "Registration failed: " + t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show()
        }
    }
}

interface ApiService {
    @POST("register")
    fun registerUser(@Body userData: UserRegistrationData): Call<ResponseBody>
}