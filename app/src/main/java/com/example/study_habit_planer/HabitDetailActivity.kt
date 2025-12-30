package com.example.study_habit_planer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HabitDetailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var habitId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_detail)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        habitId = intent.getStringExtra("habitId")

        val titleEditText = findViewById<EditText>(R.id.editTextTitle)
        val descriptionEditText = findViewById<EditText>(R.id.editTextDescription)
        val saveButton = findViewById<Button>(R.id.buttonSave)
        val deleteButton = findViewById<Button>(R.id.buttonDelete)

        if (habitId != null) {
            loadHabitData(habitId!!, titleEditText, descriptionEditText)
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            if (title.isNotEmpty()) {
                saveHabit(habitId, title, description)
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            if (habitId != null) {
                deleteHabit(habitId!!)
            }
        }
    }

    private fun loadHabitData(habitId: String, titleEditText: EditText, descriptionEditText: EditText) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("habits").document(habitId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val habit = document.toObject(Habit::class.java)
                        if (habit != null) {
                            titleEditText.setText(habit.title)
                            descriptionEditText.setText(habit.description)
                        }
                    }
                }
        }
    }

    private fun saveHabit(habitId: String?, title: String, description: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val habit = Habit(
                title = title,
                description = description,
                createdAt = System.currentTimeMillis()
            )
            val collection = db.collection("users").document(userId).collection("habits")
            val task = if (habitId != null) {
                collection.document(habitId).set(habit)
            } else {
                collection.add(habit)
            }
            task.addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to save habit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteHabit(habitId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("habits").document(habitId)
                .delete()
                .addOnSuccessListener { 
                    finish() 
                }
        }
    }
}