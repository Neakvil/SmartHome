package com.example.iamhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val token = intent.getStringExtra("token")
        val email = intent.getStringExtra("email")
        Log.i("HomePage", "UserToken:$token UserEmail:$email")

        getUserIdFromServer(email.toString())
        Log.i("HomePage", "${getUserIdFromServer(email.toString())} ")
//        val myDatasource = Device().getAllDevicesForUser()
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//
//        recyclerView.adapter = MyAdapter(this, myDatasource)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
fun getUserIdFromServer(email: String): Int {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://192.168.0.192:8080/api/v1/users?email=$email") // Замініть URL на ваш сервер та шлях до ресурсу для отримання інформації про користувачів
        .build()

    try {
        val response: Response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        // Опрацювання отриманої відповіді
        val jsonObject = JSONObject(responseBody)
        val userId = jsonObject.getInt("id") // Передполагається, що відповідь містить JSON з полем "id" для ідентифікатора користувача

        return userId

    } catch (e: Exception) {
        e.printStackTrace(System.out)
    }

    return -1 // Повертаємо -1, якщо ідентифікатор користувача не може бути отриманий
}