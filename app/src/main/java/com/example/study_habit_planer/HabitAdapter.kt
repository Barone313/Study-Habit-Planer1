package com.example.study_habit_planer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitAdapter(
    private val habits: List<Habit>,
    private val onCheckedChangeListener: (Habit, Boolean) -> Unit,
    private val onDeleteClickListener: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.titleTextView.text = habit.title
        if (habit.minutesGoal > 0) {
            holder.minutesGoalTextView.text = "${habit.minutesGoal} min"
            holder.minutesGoalTextView.visibility = View.VISIBLE
        } else {
            holder.minutesGoalTextView.visibility = View.GONE
        }

        // Prevents conflicts from recycled views
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = habit.isDoneToday

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChangeListener(habit, isChecked)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClickListener(habit)
        }
    }

    override fun getItemCount() = habits.size

    class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val minutesGoalTextView: TextView = itemView.findViewById(R.id.textViewMinutesGoal)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteHabit)
    }
}