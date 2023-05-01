package com.example.apperger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth =FirebaseAuth.getInstance()
        editEmail= findViewById(R.id.correo)
        editPassword= findViewById(R.id.password)
        btnLogin= findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            val email= editEmail.text.toString()
            val password= editPassword.text.toString()
            if (email!=""||password!=""){
                login(email, password)
            }else {
                Toast.makeText(this@Login,"Escribe un usuario valido", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun login(email: String, password:String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent: Intent= Intent(this@Login,Home::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@Login,"Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    editPassword.setText("")
                    editEmail.setText("")
                }
            }
    }



}