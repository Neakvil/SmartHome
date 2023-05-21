package com.example.iamhome.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class Device(val name: String, val status: String, val imageUrl: String) {
    // Функція, яка повертає список усіх девайсів користувача з сервера
    fun getAllDevicesForUser(userId: Int): List<Device> {
        // Створення об'єкту Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.192:8080/api/v1/devices")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Створення об'єкту сервісу для виконання запитів до сервера
        val service = retrofit.create(DeviceService::class.java)

        // Виклик методу, який повертає список девайсів користувача з сервера
        val response = service.getAllDevicesForUser(userId).execute()

        // Перетворення відповіді сервера на список девайсів у форматі класу Device
        val devices = mutableListOf<Device>()
        for (deviceResponse in response.body()!!) {
            devices.add(Device(deviceResponse.name, deviceResponse.status, deviceResponse.imageUrl))
        }

        return devices
    }
}

// Клас сервісу для виконання запитів до сервера з використанням Retrofit
interface DeviceService {
    @GET("users/{userId}/devices")
    fun getAllDevicesForUser(@Path("userId") userId: Int): Call<List<DeviceResponse>>
}

// Клас, який відповідає відповіді сервера при запиті списку девайсів користувача
data class DeviceResponse(val id: Int, val name: String, val status: String, val imageUrl: String)