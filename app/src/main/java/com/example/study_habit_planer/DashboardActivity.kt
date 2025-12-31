package com.example.study_habit_planer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        val goToHabitsButton = findViewById<Button>(R.id.buttonGoToHabits)
        val goToNotesButton = findViewById<Button>(R.id.buttonGoToNotes)
        val goToProfileButton = findViewById<Button>(R.id.buttonGoToProfile)

        goToHabitsButton.setOnClickListener {
            startActivity(Intent(this, HabitsActivity::class.java))
        }

        goToNotesButton.setOnClickListener {
            startActivity(Intent(this, NotesActivity::class.java))
        }

        goToProfileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}