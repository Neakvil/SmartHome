package com.example.iamhome.data

import android.util.Log
import com.example.iamhome.model.Device
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

interface LoadUserDeviceCallback {
    fun onUserDeviceLoaded(deviceList: List<Device>)
    fun onUserDeviceLoadError(error: String)
}

class Datasource {

    fun loadDevice(): List<Device>{
        return listOf<Device>(
            Device("string", "1"),
            Device("camEra","2")
        )
    }
    fun loadUserDevice(token: String, callback: LoadUserDeviceCallback) {
        val client = OkHttpClient()
        val url = "http://192.168.0.192:8080/api/v1/devices/get-devices-by-user-id"

        val request = Request.Builder()
            .url(url)
            .header("Authorization","Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback.onUserDeviceLoadError("Failed to load user devices: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val responseData = response.body?.string()
                    val jsonObject = JSONObject(responseData)
                    val deviceArray = jsonObject.getJSONArray("device")
                    val deviceList = mutableListOf<Device>()

                    for(i in 0 until deviceArray.length()) {
                        val deviceObject = deviceArray.getJSONObject(i)
                        val name = deviceObject.getString("name")
                        val type = deviceObject.getString("type")
                        val device = Device(name, type)
                        deviceList.add(device)
                    }

                    callback.onUserDeviceLoaded(deviceList)
                } else {
                    callback.onUserDeviceLoadError("Failed to load user devices: ${response.code}")
                }

                response.close()
            }
        })
    }
}