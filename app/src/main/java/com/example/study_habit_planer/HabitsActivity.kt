package com.example.study_habit_planer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_habit_planer.databinding.ActivityHabitsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HabitsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitsBinding
    private lateinit var habitAdapter: HabitAdapter
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Meine Gewohnheiten"

        setupRecyclerView()
        loadHabits()

        binding.fabAddHabit.setOnClickListener {
            startActivity(Intent(this, HabitDetailActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            habits = emptyList(),
            onHabitClicked = { habit ->
                val intent = Intent(this, HabitDetailActivity::class.java)
                intent.putExtra("habitId", habit.id)
                startActivity(intent)
            },
            onHabitChecked = { habit, isChecked ->
                updateHabitStatus(habit, isChecked)
            }
        )
        binding.recyclerViewHabits.apply {
            layoutManager = LinearLayoutManager(this@HabitsActivity)
            adapter = habitAdapter
        }
    }

    private fun loadHabits() {
        if (userId == null) {
            Log.e("HabitsActivity", "User not logged in.")
            return
        }

        db.collection("users").document(userId).collection("habits")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("HabitsActivity", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val habits = snapshots?.map { doc ->
                    val habit = doc.toObject(Habit::class.java)
                    habit.id = doc.id // ID für spätere Updates speichern
                    habit
                } ?: emptyList()

                habitAdapter.updateData(habits)
            }
    }

    private fun updateHabitStatus(habit: Habit, isChecked: Boolean) {
        if (userId == null || habit.id.isEmpty()) {
            Log.e("HabitsActivity", "Cannot update habit. User ID or Habit ID is missing.")
            return
        }

        db.collection("users").document(userId).collection("habits").document(habit.id)
            .update("isDoneToday", isChecked)
            .addOnSuccessListener {
                Log.d("HabitsActivity", "Habit status updated successfully.")
            }
            .addOnFailureListener { e ->
                Log.w("HabitsActivity", "Error updating habit status.", e)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
