package com.example.iamhome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.iamhome.qrscanner.QRScannerActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class HomePage : AppCompatActivity() {

    private var user_id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val sharedPreferences = getSharedPreferences("SaveUserData", Context.MODE_PRIVATE)
        val token = intent.getStringExtra("token")
        val buttonAddDevice = findViewById<Button>(R.id.button)

        buttonAddDevice.setOnClickListener { openQrScanner() }

        sendRequestToServer(token.toString())
    }

    private fun openQrScanner(){
        val randomIntent = Intent(this, QRScannerActivity::class.java)

        startActivity(randomIntent)
    }

    private fun sendRequestToServer(jwt: String) {
        val client = OkHttpClient()
        val url = "http://192.168.0.192:8080/api/v1/decode-jwt" // Replace with your server URL
        val userName = findViewById<TextView>(R.id.textHelloToUser)

        Log.i("HomePage", "Jwt:$jwt")
        Log.i("HomePage", "Data:$url")
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $jwt")
            .build()
        Log.i("HomePage", "Create request")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle network failure or server error
                Log.i("HomePage", "Suck")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val jsonObject = JSONObject(responseData)
                    Log.i("HomePage", "Json ${jsonObject}")
                    // Process the received data here
                    val userObject = jsonObject.getJSONObject("user")
                    if (userObject.has("id")) {
                        user_id = userObject.getInt("id")
                        userName.text = "Hello, ${userObject.getString("name")}!"
                        Log.i("HomePage", "User Id $user_id")
                    } else {
                        Log.i("HomePage", "Id is not create")
                    }
                } else {
                    // Handle unsuccessful response (e.g., unauthorized access)
                    // You can check response.code() for the specific HTTP status code
                }
                response.close()
            }
        })
    }
}
