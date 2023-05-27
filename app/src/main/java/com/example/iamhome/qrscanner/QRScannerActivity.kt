package com.example.iamhome.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QRScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var scannerView: ZXingScannerView

    private val CAMERA_PERMISSION_REQUEST_CODE = 101

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
        // Обробити отриманий результат
        // Наприклад, вивести в Toast
        Toast.makeText(this, scannedResult, Toast.LENGTH_SHORT).show()

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