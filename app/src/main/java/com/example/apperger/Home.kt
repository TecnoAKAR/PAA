package com.example.apperger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btn_config= findViewById<ImageButton>(R.id.configuracion)
        btn_config.setOnClickListener {
            val intent: Intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }

        val btn_chat= findViewById<Button>(R.id.chat)
        btn_chat.setOnClickListener {
            val intent: Intent = Intent(this,Chat::class.java)
            startActivity(intent)
        }

        val btn_juegos= findViewById<Button>(R.id.btn_juegos)
        btn_juegos.setOnClickListener {
            val intent: Intent = Intent(this,Minijuegos::class.java)
            startActivity(intent)
        }

        val btn_act = findViewById<Button>(R.id.actividades)
        btn_act.setOnClickListener {
            val intent: Intent = Intent(this,Estimulativas::class.java)
            startActivity(intent)
        }

    }
}