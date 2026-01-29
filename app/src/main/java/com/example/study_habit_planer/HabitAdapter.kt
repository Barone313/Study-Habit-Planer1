package com.example.study_habit_planer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Der Adapter ist die Brücke zwischen Ihren Daten (der Gewohnheiten-Liste) und der Anzeige (dem RecyclerView).
class HabitAdapter(
    // Eine Liste von Gewohnheiten, die der Adapter anzeigen soll.
    private var habits: List<Habit>,
    // Ein "Callback" - eine Möglichkeit für den Adapter, mit der Activity zu sprechen,
    // z.B. wenn eine Checkbox angeklickt wird.
    private val onHabitChecked: (Habit, Boolean) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    // Diese Methode wird aufgerufen, wenn der RecyclerView eine neue Zeile (einen ViewHolder) erstellen muss.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        // Hier wird unsere item_habit.xml-Layoutdatei "aufgeblasen" (in echten Code umgewandelt).
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    // Diese Methode wird aufgerufen, um eine Zeile mit Daten zu füllen.
    // Sie wird für jede sichtbare Zeile in der Liste aufgerufen.
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        // Holt die aktuelle Gewohnheit aus der Liste basierend auf ihrer Position.
        val habit = habits[position]
        // Füllt die UI-Elemente im ViewHolder mit den Daten der Gewohnheit.
        holder.bind(habit)
    }

    // Sagt dem RecyclerView, wie viele Einträge insgesamt in der Liste sind.
    override fun getItemCount(): Int = habits.size

    // Diese Funktion ermöglicht es der Activity, die Liste im Adapter zu aktualisieren,
    // wenn neue Daten aus Firebase geladen wurden.
    fun updateData(newHabits: List<Habit>) {
        this.habits = newHabits
        // Benachrichtigt den RecyclerView, dass sich die Daten geändert haben und er sich neu zeichnen soll.
        notifyDataSetChanged()
    }

    // --- Der ViewHolder --- //

    // Ein ViewHolder ist ein Container für die UI-Elemente einer EINZELNEN Zeile.
    // Er hält die Referenzen auf TextView, CheckBox etc., um ständiges `findViewById` zu vermeiden.
    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Referenzen auf die UI-Elemente aus item_habit.xml.
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewHabitTitle)
        private val doneCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxDone)

        // Diese Funktion füllt die UI-Elemente mit den Daten eines Habit-Objekts.
        fun bind(habit: Habit) {
            titleTextView.text = habit.title
            doneCheckBox.isChecked = habit.isDoneToday

            // Setzt einen Listener auf die Checkbox.
            doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                // Wenn der Zustand der Checkbox sich ändert, wird der Callback aufgerufen,
                // um die Activity darüber zu informieren.
                onHabitChecked(habit, isChecked)
            }
        }
    }
}
