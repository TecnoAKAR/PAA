package com.example.apperger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActivityRegistro : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        mAuth= FirebaseAuth.getInstance()
        editName=findViewById(R.id.name)
        editEmail= findViewById(R.id.correo)
        editPassword= findViewById(R.id.password)
        btnSignUp= findViewById(R.id.btn_registrar)


        btnSignUp.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val name = editName.text.toString()

            if(email!=""&&password!=""&&name!="") {
                SignUp(name, email, password)
            }else{
                Toast.makeText(this@ActivityRegistro, "Falta por escribir algun campo", Toast.LENGTH_SHORT).show()
            }
        }


    }



    private fun SignUp(name: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserTodatabase(name, email, mAuth.currentUser?.uid!!)
                   val intent = Intent(this@ActivityRegistro, Home::class.java)
                   startActivity(intent)
                } else {
                    Toast.makeText(this@ActivityRegistro,"Ocurrió un error", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserTodatabase(name: String, email: String, uid: String){
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}