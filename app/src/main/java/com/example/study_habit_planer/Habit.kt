package com.example.study_habit_planer

import com.google.firebase.firestore.PropertyName



data class Habit(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),


    val plannedMinutes: Int = 0,
    var actualMinutes: Int = 0,


    @get:PropertyName("isDoneToday") // Getter für Firebase
    @set:PropertyName("isDoneToday") // Setter für Firebase
    var isDoneToday: Boolean = false
)
