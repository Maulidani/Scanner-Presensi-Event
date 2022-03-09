package com.app.scannerpresensievent.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.scannerpresensievent.R

class ListScannedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_scanned)
        supportActionBar?.title = "Scanned"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}