package com.example.study_habit_planer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.study_habit_planer.databinding.ActivityHabitDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HabitDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var habitId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        habitId = intent.getStringExtra("habitId")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (habitId == null) "Neue Gewohnheit" else "Gewohnheit bearbeiten"

        if (habitId != null) {
            loadHabitData(habitId!!)
        }

        binding.buttonSave.setOnClickListener {
            saveHabit()
            finish()
        }
    }

    private fun loadHabitData(habitId: String) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).collection("habits").document(habitId)
            .get()
            .addOnSuccessListener { document ->
                val habit = document.toObject(Habit::class.java) ?: return@addOnSuccessListener
                binding.editTextTitle.setText(habit.title)
                binding.editTextDescription.setText(habit.description)
                binding.editTextPlannedMinutes.setText(habit.plannedMinutes.toString())
                binding.editTextActualMinutes.setText(habit.actualMinutes.toString())
                binding.switchDone.isChecked = habit.isDoneToday
            }
    }

    private fun saveHabit() {
        val title = binding.editTextTitle.text.toString()
        if (title.isEmpty()) {
            Toast.makeText(this, "Der Titel darf nicht leer sein", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid ?: return

        val description = binding.editTextDescription.text.toString()
        val plannedMinutes = binding.editTextPlannedMinutes.text.toString().toIntOrNull() ?: 0
        val actualMinutes = binding.editTextActualMinutes.text.toString().toIntOrNull() ?: 0
        val isDone = binding.switchDone.isChecked

        val habit = Habit(
            id = habitId ?: "",
            title = title,
            description = description,
            plannedMinutes = plannedMinutes,
            actualMinutes = actualMinutes,
            isDoneToday = isDone
        )

        val collection = db.collection("users").document(userId).collection("habits")
        val task = if (habitId == null) {
            collection.add(habit)
        } else {
            collection.document(habitId!!).set(habit)
        }

        task.addOnSuccessListener {
            Toast.makeText(this, "Gewohnheit gespeichert", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Fehler beim Speichern", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
