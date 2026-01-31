package com.example.study_habit_planer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitAdapter(
    private var habits: List<Habit>,
    private val onHabitClicked: (Habit) -> Unit,
    private val onHabitChecked: (Habit, Boolean) -> Unit,
    private val onHabitLongClicked: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.bind(habit)
    }

    override fun getItemCount(): Int = habits.size

    fun updateData(newHabits: List<Habit>) {
        this.habits = newHabits
        notifyDataSetChanged()
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewHabitTitle)
        private val plannedMinutesTextView: TextView = itemView.findViewById(R.id.textViewPlannedMinutes)
        private val doneCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxDone)

        fun bind(habit: Habit) {
            titleTextView.text = habit.title
            plannedMinutesTextView.text = "${habit.plannedMinutes} min"
            doneCheckBox.isChecked = habit.isDoneToday


            doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                // Verhindert, dass der Listener beim Binden der Daten ausgelöst wird
                if (doneCheckBox.isPressed) {
                    onHabitChecked(habit, isChecked)
                }
            }

            // Listener für das gesamte Item, um zur Detailansicht zu navigieren
            itemView.setOnClickListener {
                onHabitClicked(habit)
            }

            itemView.setOnLongClickListener {
                onHabitLongClicked(habit)
                true
            }
        }
    }
}
