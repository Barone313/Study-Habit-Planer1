package com.example.study_habit_planer

import com.google.firebase.firestore.PropertyName

// Diese Datenklasse repräsentiert eine einzelne Gewohnheit.

data class Habit(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),

    // Zeitmanagement
    val plannedMinutes: Int = 0, // Vom Benutzer geplante Zeit
    var actualMinutes: Int = 0,   // Tatsächlich aufgewendete Zeit

    // Status
    @get:PropertyName("isDoneToday") // Getter für Firebase
    @set:PropertyName("isDoneToday") // Setter für Firebase
    var isDoneToday: Boolean = false
)
