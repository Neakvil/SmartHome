package com.example.iamhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DeviceInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_information)

        val deviceName : TextView = findViewById(R.id.textViewDeviceName)
        val deviceType : TextView = findViewById(R.id.textViewDeviceType)


        deviceName.text = intent.getStringExtra("deviceName")
        deviceType.text = intent.getStringExtra("deviceType")
    }
}