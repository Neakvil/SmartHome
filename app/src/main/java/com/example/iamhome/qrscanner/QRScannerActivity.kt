package com.example.iamhome.qrscanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.iamhome.HomePage
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class QRScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var scannerView: ZXingScannerView

    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scannerView = ZXingScannerView(this)
        setContentView(scannerView)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Якщо дозвіл на використання камери ще не надано, запросити його
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Якщо дозвіл вже надано, запустити сканер
            startScanner()
        }
    }

    private fun startScanner() {
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun handleResult(result: Result?) {
        // Отримано результат сканування
        val scannedResult: String? = result?.text
        val token = intent.getStringExtra("token")
        // Обробити отриманий результат
        // Наприклад, вивести в Toast
        //Toast.makeText(this, scannedResult, Toast.LENGTH_SHORT).show()

        val requestBody = FormBody.Builder()
            .add("mpn", scannedResult.toString())
            .build()

        val url = "http://192.168.0.192:8080/api/v1/devices/add-device-by-mpn"
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .post(requestBody)
            .build()


        Log.i("qrScanner", "request $request")

        Log.i("qrScanner", "token $token")

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.i("qrScanner", "FuckIs")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    Log.i("qrScanner", "Start Secsess")
                    runOnUiThread {
                        Toast.makeText(
                            this@QRScannerActivity,
                            "данні успушно надіслані",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish();
                    }
                }else{
                    runOnUiThread {
                        Toast.makeText(
                            this@QRScannerActivity,
                            "You already have this device!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        })
        // Перезапустити сканер для подальшого сканування
        scannerView.resumeCameraPreview(this)
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera() // Зупинити камеру при паузі Activity
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Дозвіл надано, запустити сканер
                startScanner()
            } else {
                // Дозвіл не надано, показати повідомлення або вжити відповідні дії
            }
        }
    }
}