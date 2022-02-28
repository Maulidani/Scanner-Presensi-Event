package com.app.scannerpresensievent

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.app.scannerpresensievent.ui.ScannerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        CoroutineScope(Dispatchers.Main).launch {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                delay(1000)
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 1);

            } else {
                delay(1000)
                startActivity(Intent(this@MainActivity, ScannerActivity::class.java))
                finish()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                delay(1000)
                startActivity(Intent(this@MainActivity, ScannerActivity::class.java))
                finish()
            }
        }
    }
}