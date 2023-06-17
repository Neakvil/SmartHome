package com.example.iamhome

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.Random
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
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
        var url : String = "http://192.168.0.192:8080/api/v1/temperature/temperature-statistics/"
        val token = intent.getStringExtra("token")

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val deviceName : TextView = findViewById(R.id.textViewDeviceName)
        val deviceType : TextView = findViewById(R.id.textViewDeviceType)
        val linearLayoutTemperature = findViewById<LinearLayout>(R.id.linearLayoutTemperature)
        val linearLayoutHumidity = findViewById<LinearLayout>(R.id.linearLayoutHumidity)
        val chartButton = findViewById<Button>(R.id.buttonChart)
        barChart = findViewById(R.id.barChart)

        updateData()

        linearLayoutTemperature.setOnClickListener {
             url = "http://192.168.0.192:8080/api/v1/temperature/temperature-statistics/"
            runOnUiThread{
                Toast.makeText(this, "Your see temperature statistics in charts", Toast.LENGTH_SHORT).show()
            }
        }

        linearLayoutHumidity.setOnClickListener {
            url = "http://192.168.0.192:8080/api/v1/humidity/humidity-statistics/"
            runOnUiThread{
                Toast.makeText(this, "Your see humidity statistics in charts", Toast.LENGTH_SHORT).show()
            }
        }

        chartButton.setOnClickListener {
            chartInformationWithRadioButton(token.toString(), url)
        }

        swipeRefreshLayout.setOnRefreshListener {
            // Код для оновлення даних

            updateData()

            // Після завершення оновлення викличте метод setRefreshing(false),
            // щоб зупинити анімацію оновлення
            swipeRefreshLayout.isRefreshing = false
        }

        deviceName.text = intent.getStringExtra("deviceName")
        deviceType.text = intent.getStringExtra("deviceType")

    }

    fun chartInformationWithRadioButton(token: String, url : String) {

        val timeOption = findViewById<RadioGroup>(R.id.time_options)

        val timeInChartInformation = when(timeOption.checkedRadioButtonId){
            R.id.option_one_hour -> oneHourTemperatureInformation
            R.id.option_two_hour -> twoHourTemperatureInformation
            R.id.option_four_hour -> fourHourTemperatureInformation
            else -> eightHourTemperatureInformation
        }

        if(url == "http://192.168.0.192:8080/api/v1/temperature/temperature-statistics/"){
            updateDataInChartsTemperature(token, url, timeInChartInformation)
        }else {
            updateDataInChartsHumidity(token, url, timeInChartInformation)
        }

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

    fun updateDataInChartsHumidity(token : String, url : String, delay : Int){

        val requestBody = FormBody.Builder()
            .add("delay", delay.toString())
            .build()

        Log.i("DeviceInformationHumidity", "$token")
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    e.printStackTrace()
                    Log.i("DeviceInformationHumidity", "${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val responseData = response.body?.string()
                    val jsonObject = JSONObject(responseData)
                    Log.i("DeviceInformationHumidity", "$responseData")
                    val temperatures = jsonObject.getJSONArray("humidity")

                    runOnUiThread {
                        barChart.data = null
                        val entries = ArrayList<BarEntry>()
                        for (i in 0 until temperatures.length()) {
                            val temperatureJson = temperatures.getJSONObject(i)
                            val temperatureString = temperatureJson.getString("humidity")
                            val temperature = temperatureString.toFloat()
                            entries.add(BarEntry(i.toFloat(), temperature))
                        }

                        val dataSet = BarDataSet(entries, "Вологість")
                        dataSet.color = Color.parseColor("#FDA43C")
                        dataSet.valueTextColor = Color.parseColor("#FDA43C")

                        val barData = BarData(dataSet)
                        barChart.data = barData

                        val xAxis = barChart.xAxis
                        val leftYAxis = barChart.axisLeft
                        val rightYAxis = barChart.axisRight

                        // Налаштування коліра міток на осі X
                        xAxis.textColor = Color.parseColor("#FDA43C")// Задайте бажаний колір для міток на осі X

                        // Налаштування коліра міток на осі Y
                        leftYAxis.textColor = Color.parseColor("#FDA43C")
                        rightYAxis.textColor = Color.parseColor("#FDA43C")

                        // Налаштування графіку
                        barChart.description.isEnabled = false
                        barChart.setFitBars(true)
                        barChart.animateY(1000)
                        barChart.invalidate()
                    }

                }else{
                    Log.i("DeviceInformationHumidity", "Suck2.0")
                }
            }

        })

    }

    fun updateDataInChartsTemperature(token : String, url : String, delay : Int){

        val requestBody = FormBody.Builder()
            .add("delay", delay.toString())
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
                    val jsonObject = JSONObject(responseData)
                    val temperatures = jsonObject.getJSONArray("temperature")

                    runOnUiThread {
                        barChart.data = null
                        val entries = ArrayList<BarEntry>()
                        for (i in 0 until temperatures.length()) {
                            val temperatureJson = temperatures.getJSONObject(i)
                            val temperatureString = temperatureJson.getString("temperature")
                            val temperature = temperatureString.toFloat()
                            entries.add(BarEntry(i.toFloat(), temperature))
                        }

                        val dataSet = BarDataSet(entries, "Температура")
                        dataSet.color = Color.parseColor("#FDA43C")
                        dataSet.valueTextColor = Color.parseColor("#FDA43C")

                        val barData = BarData(dataSet)
                        barChart.data = barData

                        val xAxis = barChart.xAxis
                        val leftYAxis = barChart.axisLeft
                        val rightYAxis = barChart.axisRight

                        // Налаштування коліра міток на осі X
                        xAxis.textColor = Color.parseColor("#FDA43C")// Задайте бажаний колір для міток на осі X

                        // Налаштування коліра міток на осі Y
                        leftYAxis.textColor = Color.parseColor("#FDA43C")
                        rightYAxis.textColor = Color.parseColor("#FDA43C")

                        // Налаштування графіку
                        barChart.description.isEnabled = false
                        barChart.setFitBars(true)
                        barChart.animateY(1000)
                        barChart.invalidate()
                    }

                }else{
                    Log.i("DeviceInformationTemperatyra", "Suck2.0")
                }
            }

        })

    }


}