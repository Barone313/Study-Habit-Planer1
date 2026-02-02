package com.example.study_habit_planer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        auth = FirebaseAuth.getInstance()


        if (auth.currentUser != null) {

            startActivity(Intent(this, DashboardActivity::class.java))
        } else {

            startActivity(Intent(this, LoginActivity::class.java))
        }


        finish()
    }
}
