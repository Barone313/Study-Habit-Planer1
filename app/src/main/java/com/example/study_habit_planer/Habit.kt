package com.example.study_habit_planer

data class Habit(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val createdAt: Long = 0,
    var isDoneToday: Boolean = false,
    val minutesGoal: Int = 0
)