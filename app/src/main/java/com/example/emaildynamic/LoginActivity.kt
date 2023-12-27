package com.example.emaildynamic

import com.example.emaildynamic.MainActivity
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
val auth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)


        loginBtn.setOnClickListener {

            LoginPage(email.text.toString(), password.text.toString())
        }

        var singBtn = findViewById<TextView>(R.id.sigbup)
        singBtn.setOnClickListener {
            Toast.makeText(this, "Registation Form", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegistationActivity::class.java)
            startActivity(intent)
        }
    }
    fun LoginPage(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}