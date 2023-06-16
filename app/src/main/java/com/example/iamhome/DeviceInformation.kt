package com.example.iamhome

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.Random
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

@Suppress("DEPRECATION")
class DeviceInformation : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private lateinit var barChart: BarChart
    private val client = OkHttpClient()

    companion object{
        const val oneHourTemperatureInformation = 60
        const val twoHourTemperatureInformation = 120
        const val fourHourTemperatureInformation = 240
        const val eightHourTemperatureInformation = 480
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_information)

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val deviceName : TextView = findViewById(R.id.textViewDeviceName)
        val deviceType : TextView = findViewById(R.id.textViewDeviceType)

        updateData()

        swipeRefreshLayout.setOnRefreshListener {
            // Код для оновлення даних

            updateData()

            // Після завершення оновлення викличте метод setRefreshing(false),
            // щоб зупинити анімацію оновлення
            swipeRefreshLayout.isRefreshing = false
        }

        deviceName.text = intent.getStringExtra("deviceName")
        deviceType.text = intent.getStringExtra("deviceType")
        val token = intent.getStringExtra("token")

        updateDataInChartsTemperature(token.toString())

        barChart = findViewById(R.id.barChart)

        // Приклад даних температури
        val temperatures = listOf(20f, 25f, 22f, 18f, 23f)

        val entries = ArrayList<BarEntry>()
        for (i in temperatures.indices) {
            entries.add(BarEntry(i.toFloat(), temperatures[i]))
        }

        val dataSet = BarDataSet(entries, "Температура")
        dataSet.color = Color.BLUE

        val barData = BarData(dataSet)
        barChart.data = barData

        // Налаштування графіку
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.animateY(1000)
        barChart.invalidate()

    }

    fun updateData() {

        val random = Random()
        val temperature = random.nextInt(50).toString()
        val humidity = random.nextInt(20).toString()

        val textViewTemperature = findViewById<TextView>(R.id.textViewTemperature)
        val textViewHumidity = findViewById<TextView>(R.id.textViewHumidity)

        textViewTemperature.text = "$temperature°"
        textViewHumidity.text = "$humidity%"
    }

    fun updateDataInChartsTemperature(token : String){

        val url = "http://192.168.0.192:8080/api/v1/temperature/temperature-statistics/"
        val requestBody = FormBody.Builder()
            .add("delay", oneHourTemperatureInformation.toString())
            .build()

        Log.i("DeviceInformationTemperatyra", "$token")
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    e.printStackTrace()
                    Log.i("DeviceInformationTemperatyra", "${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val responseData = response.body?.string()
                    Log.i("DeviceInformationTemperatyra", "$responseData")
                }else{
                    Log.i("DeviceInformationTemperatyra", "Suck2.0")
                }
            }

        })

    }


}