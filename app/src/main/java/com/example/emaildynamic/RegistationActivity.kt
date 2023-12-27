package com.example.emaildynamic

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegistationActivity : AppCompatActivity() {
    var auth= FirebaseAuth.getInstance()
    val db=FirebaseFirestore.getInstance()
    private var point = "0"
    private lateinit var editname1:EditText
    lateinit var editemail:EditText
    private lateinit var editage:EditText
    private lateinit var editpass:EditText
    lateinit var editphone:EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registation)

        dynamicLinkReceive()

        editname1=findViewById(R.id.userName)
        editemail=findViewById(R.id.userEmail)
        editage=findViewById(R.id.userAge)
        editpass=findViewById(R.id.userPassword)
        editphone=findViewById(R.id.userPhone)

        var btn=findViewById<Button>(R.id.button)

        btn.setOnClickListener {
            registerUsers()
        }
        val loginBtn = findViewById<TextView>(R.id.loginB)
        loginBtn.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun registerUsers(){
        val name = editname1.text.toString()
        val email = editemail.text.toString()
        val age = editage.text.toString().toLong()
        val password = editpass.text.toString()
        val phone = editphone.text.toString().toLong()
        if (email.isEmpty() && password.isEmpty()){
            Toast.makeText(this, "Enter field", Toast.LENGTH_SHORT).show()
        }
        else{

            auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    val id =it.user?.uid.toString()
                    val userModel = UserModel(name,email,age,password,phone,id,point)

                    db.collection("user_details").document(id).set(userModel)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registation Successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,LoginActivity::class.java))
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun dynamicLinkReceive(){
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) {pendingDynamicLinkData : PendingDynamicLinkData? ->
                var deepLink:Uri?=null
                if (pendingDynamicLinkData !=null){
                    deepLink = pendingDynamicLinkData.link
                }
                if(deepLink!=null){
                    point = deepLink.getQueryParameter("point").toString()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}