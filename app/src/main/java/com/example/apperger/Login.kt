package com.example.apperger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_login= findViewById<Button>(R.id.btn_login)
        btn_login.setOnClickListener {
            val intent: Intent= Intent(this,Home::class.java)
            startActivity(intent)
        }
    }
}