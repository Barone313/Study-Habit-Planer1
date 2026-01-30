package com.example.study_habit_planer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.study_habit_planer.databinding.ActivityDashboardBinding // Import der neuen Binding-Klasse
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityDashboardBinding // Der "persönliche Assistent" für Ihr Layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Der neue, moderne Weg, das Layout zu laden
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Direkter und sicherer Zugriff auf die Buttons über das binding-Objekt
        binding.buttonGoToProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.buttonGoToHabits.setOnClickListener {
            startActivity(Intent(this, HabitsActivity::class.java))
        }

        binding.buttonGoToNotes.setOnClickListener {
            startActivity(Intent(this, NotesActivity::class.java))
        }


    }
}