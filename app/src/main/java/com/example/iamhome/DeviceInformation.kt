package com.example.iamhome

import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.TextView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Random

class DeviceInformation : AppCompatActivity() {

    private lateinit var temperatureGraphSurfaceView: TemperatureGraphSurfaceView

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_information)

        Log.i("Temperature","Start on create")
        temperatureGraphSurfaceView = findViewById(R.id.graphSurfaceView)

        Log.i("Temperature","temperatureGraphSurfaceView = $temperatureGraphSurfaceView")
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

        // Отримання даних про температуру за минулий тиждень
        val temperatureData = getTemperatureData()
        Log.i("Temperature","temperatureData = $temperatureData")
        // Встановлення даних в SurfaceView
        temperatureGraphSurfaceView.setTemperatureData(temperatureData)

        // Виклик drawTemperatureGraph() після повної ініціалізації SurfaceView
        temperatureGraphSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                GlobalScope.launch {
                    temperatureGraphSurfaceView.drawTemperatureGraph()
                    Log.i("Temperature", "${temperatureGraphSurfaceView.drawTemperatureGraph()}")
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                GlobalScope.launch {
                    temperatureGraphSurfaceView.drawTemperatureGraph()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })
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

    private fun getTemperatureData(): List<Float> {
        // Отримання даних про температуру за минулий тиждень
        // Вам потрібно реалізувати цю функцію для отримання даних зі свого джерела
        // Наприклад, ви можете отримати дані з сервера або зберігати їх локально

        // Повертаємо прикладові дані (від -10 до 30)
        return (0..7).map { it.toFloat() * 5 - 10 }
    }

}