package com.example.iamhome

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class qrCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        val qrCodeImageView: ImageView = findViewById(R.id.qrCodeImageView)
        val sendQrToGmail: Button = findViewById(R.id.sendQrToGmail)

        val text = intent.getStringExtra("deviceMPN")

        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix: BitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 600, 600)

            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)

            // Змінити колір фону QR-коду на білий
            val backgroundColor = Color.WHITE

            // Змінити колір пікселів QR-коду на червоний
            val foregroundColor = Color.parseColor("#FDA43C")

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) foregroundColor else backgroundColor
                }
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

            qrCodeImageView.setImageBitmap(bitmap)

            sendQrToGmail.setOnClickListener {
                val qrCodeFile = saveBitmapToFile(bitmap)

                if (qrCodeFile != null) {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "application/image"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "QR Code")
                    intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, "${packageName}.fileprovider", qrCodeFile))
                    startActivity(Intent.createChooser(intent, "Надіслати за допомогою"))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File? {
        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()
        val qrCodeFile = File(cachePath, "qrcode.png")

        try {
            val outputStream = FileOutputStream(qrCodeFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return qrCodeFile
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}