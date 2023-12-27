package com.example.emaildynamic

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    val auth =FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//
//        val localStorage = getSharedPreferences("user", Context.MODE_PRIVATE)
//        val status=localStorage.getBoolean("loginStatus",false)

        Handler().postDelayed(Runnable {
            if (auth.currentUser?.uid != null){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
           else {
                val intent = Intent(this,RegistationActivity ::class.java)
                startActivity(intent)
            }
        },2000)

    }
}