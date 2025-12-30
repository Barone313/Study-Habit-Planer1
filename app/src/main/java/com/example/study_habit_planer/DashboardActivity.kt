package com.example.study_habit_planer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var notepadEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        notepadEditText = findViewById(R.id.editTextNotepad)
        val goToHabitsButton = findViewById<Button>(R.id.buttonGoToHabits)
        val goToProfileButton = findViewById<Button>(R.id.buttonGoToProfile)
        val saveNoteButton = findViewById<Button>(R.id.buttonSaveNote)

        goToHabitsButton.setOnClickListener {
            startActivity(Intent(this, HabitsActivity::class.java))
        }

        goToProfileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        saveNoteButton.setOnClickListener {
            saveNote()
        }

        loadNote()
    }

    private fun saveNote() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Fehler: Nicht angemeldet", Toast.LENGTH_SHORT).show()
            return
        }

        val noteText = notepadEditText.text.toString()
        val userDocRef = db.collection("users").document(currentUser.uid)

        val noteData = hashMapOf("quick_note" to noteText)

        userDocRef.set(noteData) // Use set with merge option or update
            .addOnSuccessListener { 
                Toast.makeText(this, "Notiz gespeichert!", Toast.LENGTH_SHORT).show() 
            }
            .addOnFailureListener { e -> 
                Toast.makeText(this, "Fehler beim Speichern: ${e.message}", Toast.LENGTH_LONG).show() 
            }
    }

    private fun loadNote() {
        val currentUser = auth.currentUser
        if (currentUser == null) return

        val userDocRef = db.collection("users").document(currentUser.uid)

        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val noteText = document.getString("quick_note")
                    notepadEditText.setText(noteText)
                }
            }
            .addOnFailureListener { e ->
                // Don't show an error on load, it might just be the first time
            }
    }
}