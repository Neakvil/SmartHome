package com.example.iamhome

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class NewDevice : AppCompatActivity() {

    private val client = OkHttpClient()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_device)

        val buttonAddedNewDevice : Button = findViewById(R.id.buttonAddedNewDevice)
        val deviceEditTextName : EditText = findViewById(R.id.editTextDeviceName)
        val deviceEditTextType : EditText = findViewById(R.id.editTextDeviceType)
        val deviceEditTextMPN : EditText = findViewById(R.id.editTextDeviceMPN)

        buttonAddedNewDevice.setOnClickListener { sendDeviceToServer(deviceEditTextName.text.toString(),deviceEditTextType.text.toString(), deviceEditTextMPN.text.toString() ) }
    }

    fun sendDeviceToServer(deviceName : String, deviceType : String, deviceMPN : String){

        if(deviceName != null || deviceType != null || deviceMPN != null)
        {
            val url = "http://192.168.0.192:8080/api/v1/devices/add"

            val requestBody = FormBody.Builder()
                .add("name" , deviceName)
                .add("type", deviceType)
                .add("mpn", deviceMPN)
                .build()

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@NewDevice, "Failed to added device", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        Toast.makeText(this@NewDevice, "Device added is success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@NewDevice, qrCode::class.java)
                        intent.putExtra("deviceMPN", deviceMPN)
                        finish()

                        startActivity(intent)
                    }
                }

            })
        }else {
            runOnUiThread {
                Toast.makeText(this@NewDevice, "Pls type data in all fields!s", Toast.LENGTH_SHORT).show()
            }
        }
    }

}