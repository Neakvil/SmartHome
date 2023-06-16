package com.example.iamhome

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class TemperatureGraphSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private var temperatureData: List<Float>? = null // Дані температури

    init {
        holder.addCallback(this)
    }

    fun setTemperatureData(data: List<Float>) {
        temperatureData = data
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        drawTemperatureGraph()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        drawTemperatureGraph()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    fun drawTemperatureGraph() {
        val data = temperatureData ?: return
        val canvas = holder.lockCanvas() ?: return

        try {
            canvas.drawColor(Color.WHITE)

            val graphHeight = canvas.height.toFloat()
            val graphWidth = canvas.width.toFloat()

            val xInterval = graphWidth / (data.size - 1)

            val paint = Paint().apply {
                color = Color.BLACK
                strokeWidth = 2f
            }

            var prevX = 0f
            var prevY = graphHeight - data[0]

            for (i in 1 until data.size) {
                val x = i * xInterval
                val y = graphHeight - data[i]

                canvas.drawLine(prevX, prevY, x, y, paint)

                prevX = x
                prevY = y
            }
        } finally {
            holder.unlockCanvasAndPost(canvas)
        }
    }
}
