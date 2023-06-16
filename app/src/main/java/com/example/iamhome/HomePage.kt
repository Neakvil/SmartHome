package com.example.iamhome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iamhome.adapter.DeviceAdapter
import com.example.iamhome.data.Datasource
import com.example.iamhome.data.LoadUserDeviceCallback
import com.example.iamhome.model.Device
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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewDevices)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        val token = intent.getStringExtra("token")

        val buttonAddDevice = findViewById<Button>(R.id.button)
        buttonAddDevice.setOnClickListener { showPopupDialog(token.toString()) }

        val myDatasource = Datasource()

        myDatasource.loadUserDevice(token.toString(), object: LoadUserDeviceCallback {
            override fun onUserDeviceLoaded(deviceList: List<Device>) {
                // Оновити адаптер з отриманим списком пристроїв
                Log.i("HomePage", " device List $deviceList")

                var adapter = DeviceAdapter(deviceList)
                recyclerView.adapter = adapter
                adapter.setOnItemClickListener(object :  DeviceAdapter.onItemClickListener {

                    override fun onItemClick(position: Int) {
                        //Toast.makeText(this@HomePage, "Your Clicked on item no. $position", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@HomePage, DeviceInformation::class.java)
                        intent.putExtra("deviceName", deviceList[position].name)
                        intent.putExtra("deviceType", deviceList[position].type)
                        startActivity(intent)
                    }
                })
            }

            override fun onUserDeviceLoadError(error: String) {
                // Обробити помилку завантаження даних
                Log.i("HomePage", "Error loading user devices: $error")
            }
        })

        sendRequestToServer(token.toString())
    }

    override fun onStart() {
        super.onStart()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewDevices)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        val token = intent.getStringExtra("token")
        Log.i("Token" , "$token")

        val myDatasource = Datasource()

        myDatasource.loadUserDevice(token.toString(), object: LoadUserDeviceCallback {
            override fun onUserDeviceLoaded(deviceList: List<Device>) {
                // Оновити адаптер з отриманим списком пристроїв
                Log.i("HomePage", " device List $deviceList")

                var adapter = DeviceAdapter(deviceList)
                recyclerView.adapter = adapter
                adapter.setOnItemClickListener(object :  DeviceAdapter.onItemClickListener {

                    override fun onItemClick(position: Int) {
                        //Toast.makeText(this@HomePage, "Your Clicked on item no. $position", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@HomePage, DeviceInformation::class.java)
                        intent.putExtra("deviceName", deviceList[position].name)
                        intent.putExtra("deviceType", deviceList[position].type)
                        startActivity(intent)
                    }
                })
            }

            override fun onUserDeviceLoadError(error: String) {
                // Обробити помилку завантаження даних
                Log.i("HomePage", "Error loading user devices: $error")
                // Додайте блок catch для перехоплення винятків
                try {
                    throw Exception(error) // Створіть виняток для отримання додаткової інформації про помилку
                } catch (e: Exception) {
                    e.printStackTrace() // Виведіть стек викликів винятків у логи
                }
            }
        })
        sendRequestToServer(token.toString())

    }

    private fun showPopupDialog(token: String) {
        val actions = arrayOf("Added new device", "Scanner QR code")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select an action")
            .setItems(actions) { _, which ->
                when (which) {
                    0 -> openAddNewDevice()
                    1 -> openQrScanner(token)
                }
            }
            .show()
    }

    private fun openAddNewDevice() {
        val intent = Intent(this, NewDevice::class.java)
        startActivity(intent)
    }

    private fun openQrScanner(token:String){
        val randomIntent = Intent(this, QRScannerActivity::class.java)
        randomIntent.putExtra("token", token)
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
