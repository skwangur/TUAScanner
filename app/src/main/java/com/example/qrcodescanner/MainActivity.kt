package com.example.qrcodescanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the floating action button
        floatingActionButton = findViewById(R.id.floatingActionButton)

        registerUiListeners()
    }

    private fun registerUiListeners() {
        // Floating Action Button click to scan QR
        floatingActionButton.setOnClickListener {
            launchQrScanner()
        }
    }

    private fun launchQrScanner() {
        scannerLauncher.launch(
            ScanOptions().setPrompt("Scan QR Code")
                .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        )
    }

    private val scannerLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        } else {
            val scannedValue = result.contents

            // Check if the scanned value is a URL
            if (scannedValue.startsWith("http://") || scannedValue.startsWith("https://")) {
                openUrl(scannedValue)
            } else {
                Toast.makeText(this, "Scanned value is not a URL.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        // Check if there's an app to handle the intent
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No application can handle this request.", Toast.LENGTH_SHORT).show()
        }
    }
}
