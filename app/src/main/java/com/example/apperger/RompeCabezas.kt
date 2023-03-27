package com.example.apperger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RompeCabezas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rompe_cabezas)
        supportActionBar?.hide()

    }
}