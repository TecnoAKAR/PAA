package com.example.apperger
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Minijuegos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minijuegos)


        val btn_rompe= findViewById<ImageButton>(R.id.btn_rompe)
        btn_rompe.setOnClickListener {
            val intent: Intent = Intent(this,RompeCabezas::class.java)
            startActivity(intent)
        }

        val btn_simon= findViewById<ImageButton>(R.id.btn_simon)
        btn_simon.setOnClickListener {
            val intent: Intent = Intent(this,SimonDice::class.java)
            startActivity(intent)
        }

        val btn_ajedrez= findViewById<ImageButton>(R.id.btn_ajedrez)
        btn_ajedrez.setOnClickListener {
            val intent: Intent = Intent(this,Ajedrez::class.java)
            startActivity(intent)
        }

        val btn_pintura= findViewById<ImageButton>(R.id.btn_pinturillo)
        btn_pintura.setOnClickListener {
            val intent: Intent = Intent(this,Pinturillo::class.java)
            startActivity(intent)
        }


    }
}