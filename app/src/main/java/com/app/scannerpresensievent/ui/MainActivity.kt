package com.app.scannerpresensievent.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.scannerpresensievent.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val btnStartScan: MaterialButton by lazy { findViewById(R.id.btnStartScan) }
    val btnScanned: MaterialButton by lazy { findViewById(R.id.btnScanned) }
    val btnAbout: MaterialButton by lazy { findViewById(R.id.btnAbout) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1);
        }

        btnStartScan.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Izinkan Permission Camera", Toast.LENGTH_SHORT)
                    .show()
            } else {

                startActivity(
                    Intent(
                        applicationContext,
                        ScannerActivity::class.java
                    )
                )
            }

        }
        btnScanned.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    ListScannedActivity::class.java
                )
            )
        }
        btnAbout.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    AboutActivity::class.java
                )
            )
        }
    }
}