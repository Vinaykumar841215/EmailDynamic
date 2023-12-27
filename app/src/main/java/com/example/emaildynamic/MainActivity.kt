package com.example.emaildynamic

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var shortLink=""
    val db= FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var logoutBtn = findViewById<ImageView>(R.id.logout)

        logoutBtn.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Logout")
            alertDialogBuilder.setMessage("Are you sure you want to logout?")

            alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                auth.signOut()
                Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }

            alertDialogBuilder.setNegativeButton("No") { dialog, which ->
                // Do nothing or handle the case where the user cancels the logout
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        val sendBtn=findViewById<ImageView>(R.id.shortBtn)

        val  point=findViewById<TextView>(R.id.showPoint)
        val getName = findViewById<TextView>(R.id.nameText)
        val getEmail = findViewById<TextView>(R.id.emailText)
        val getAge = findViewById<TextView>(R.id.ageText)
        val getPhone = findViewById<TextView>(R.id.phoneText)

        val id=auth.currentUser?.uid.toString()
        db.collection("user_details").document(id).get()

            .addOnSuccessListener {
                val result = it.toObject(UserModel::class.java)

                point.text ="Point :- " +result?.point.toString()

                getName.text=  result?.name.toString()
                getEmail.text ="Email :- " + result?.email.toString()
                getAge.text ="Age :- " +result?.age.toString()
                getPhone.text="Phone :- " +result?.phone.toString()
            }


        val intentFilter= IntentFilter(Intent.ACTION_VIEW)
        intentFilter.addDataScheme("https")
        intentFilter.addDataAuthority("google.com",null)


        Firebase.dynamicLinks.shortLinkAsync {

            link= Uri.parse("https://google.com/?point=100")
            domainUriPrefix="https://mydynamic.page.link"
            androidParameters {
            }

            iosParameters("com.example.ios"){}
        }.addOnSuccessListener {
            shortLink=it.shortLink.toString()

        }.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }

        sendBtn.setOnClickListener {
            val intent= Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_SUBJECT,"App Link")
            intent.putExtra(Intent.EXTRA_TEXT,shortLink)
            startActivity(Intent(Intent.createChooser(intent,"Dynamic Link App")))
        }
    }
}