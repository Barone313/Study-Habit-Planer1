package com.example.study_habit_planer

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HabitsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var habitAdapter: HabitAdapter
    private val habitsList = mutableListOf<Habit>()

    private lateinit var newHabitTitleEditText: EditText
    private lateinit var newHabitMinutesGoalEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habits)

        // Add back button to the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Meine Gewohnheiten"

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        newHabitTitleEditText = findViewById(R.id.editTextNewHabitTitle)
        newHabitMinutesGoalEditText = findViewById(R.id.editTextMinutesGoal)
        val addNewHabitButton = findViewById<Button>(R.id.buttonAddNewHabit)
        val habitsRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewHabits)

        habitsRecyclerView.layoutManager = LinearLayoutManager(this)

        habitAdapter = HabitAdapter(
            habitsList,
            { habit, isChecked -> updateHabitStatus(habit, isChecked) },
            { habit -> deleteHabit(habit) }
        )
        habitsRecyclerView.adapter = habitAdapter

        addNewHabitButton.setOnClickListener {
            val title = newHabitTitleEditText.text.toString()
            val minutesString = newHabitMinutesGoalEditText.text.toString()
            val minutes = if (minutesString.isNotEmpty()) minutesString.toInt() else 0

            if (title.isNotEmpty()) {
                addNewHabit(title, minutes)
                newHabitTitleEditText.text.clear()
                newHabitMinutesGoalEditText.text.clear()
            } else {
                Toast.makeText(this, "Bitte einen Titel eingeben", Toast.LENGTH_SHORT).show()
            }
        }

        loadHabits()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle back button click
        if (item.itemId == android.R.id.home) {
            finish() // This will take the user back to the DashboardActivity
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewHabit(title: String, minutesGoal: Int) {
        val currentUser = auth.currentUser ?: return

        val newHabit = Habit(
            title = title,
            description = "",
            createdAt = System.currentTimeMillis(),
            isDoneToday = false,
            minutesGoal = minutesGoal
        )

        db.collection("users").document(currentUser.uid).collection("habits")
            .add(newHabit)
            .addOnSuccessListener { Toast.makeText(this, "Gewohnheit hinzugefügt!", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(this, "Fehler: ${e.message}", Toast.LENGTH_LONG).show() }
    }

    private fun updateHabitStatus(habit: Habit, isDone: Boolean) {
        val currentUser = auth.currentUser
        if (currentUser == null || habit.id.isEmpty()) return

        db.collection("users").document(currentUser.uid).collection("habits").document(habit.id)
            .update("isDoneToday", isDone) // Corrected field name
            .addOnFailureListener { e ->
                Toast.makeText(this, "Status konnte nicht gespeichert werden: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteHabit(habit: Habit) {
        val currentUser = auth.currentUser
        if (currentUser == null || habit.id.isEmpty()) {
            Toast.makeText(this, "Fehler: Kann Gewohnheit nicht löschen", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users").document(currentUser.uid).collection("habits").document(habit.id)
            .delete()
            .addOnSuccessListener { Toast.makeText(this, "Gewohnheit gelöscht", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(this, "Fehler beim Löschen: ${e.message}", Toast.LENGTH_LONG).show() }
    }

    private fun loadHabits() {
        val currentUser = auth.currentUser ?: return

        db.collection("users").document(currentUser.uid).collection("habits")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Fehler beim Laden: ${e.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                habitsList.clear()
                snapshots?.mapNotNull { it.toObject(Habit::class.java).apply { id = it.id } }?.let { habitsList.addAll(it) }
                habitAdapter.notifyDataSetChanged()
            }
    }
}