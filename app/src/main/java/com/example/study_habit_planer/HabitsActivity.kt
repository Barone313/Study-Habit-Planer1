package com.example.study_habit_planer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_habit_planer.databinding.ActivityHabitsBinding // Import der ViewBinding-Klasse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HabitsActivity : AppCompatActivity() {

    // --- Variablen-Deklaration ---

    // ViewBinding-Objekt für den sicheren Zugriff auf die Layout-Elemente.
    private lateinit var binding: ActivityHabitsBinding

    // Eine Instanz der Firebase-Datenbank.
    private lateinit var db: FirebaseFirestore

    // Eine Instanz für die Firebase-Authentifizierung, um den aktuellen Benutzer zu bekommen.
    private lateinit var auth: FirebaseAuth

    // Der Adapter, der die Daten mit der Liste (RecyclerView) verbindet.
    private lateinit var habitAdapter: HabitAdapter

    // Eine lokale Liste, um die geladenen Gewohnheiten zu speichern.
    private val habitList = mutableListOf<Habit>()

    // --- Activity-Lebenszyklus ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Layout mit ViewBinding aufbauen.
        binding = ActivityHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Firebase-Instanzen initialisieren.
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // 3. Den Zurück-Pfeil in der oberen Leiste (ActionBar) anzeigen.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Meine Gewohnheiten"

        // 4. Den RecyclerView und den Adapter einrichten.
        setupRecyclerView()

        // 5. Listener für den "+"-Button einrichten (die Logik kommt später).
        binding.fabAddHabit.setOnClickListener {
            // TODO: Dialog oder neue Activity zum Hinzufügen einer Gewohnheit öffnen.
            Toast.makeText(this, "Funktion zum Hinzufügen kommt bald!", Toast.LENGTH_SHORT).show()
        }

        // 6. Die Gewohnheiten aus Firebase laden.
        loadHabits()
    }

    // --- Private Hilfsfunktionen ---

    private fun setupRecyclerView() {
        // Den Adapter initialisieren. Wir übergeben ihm eine leere Liste und einen Callback.
        habitAdapter = HabitAdapter(habitList) { habit, isChecked ->
            // Dieser Code-Block wird ausgeführt, wenn eine Checkbox im Adapter geklickt wird.
            updateHabitStatus(habit, isChecked)
        }
        // Den RecyclerView im Layout mit unserem Adapter und einem LayoutManager verbinden.
        binding.recyclerViewHabits.apply {
            layoutManager = LinearLayoutManager(this@HabitsActivity)
            adapter = habitAdapter
        }
    }

    private fun loadHabits() {
        // Den aktuellen Benutzer holen. Wenn niemand angemeldet ist, abbrechen.
        val currentUser = auth.currentUser ?: return

        // Den Pfad zur "habits"-Sammlung des aktuellen Benutzers in Firestore definieren.
        db.collection("users").document(currentUser.uid).collection("habits")
            .orderBy("createdAt", Query.Direction.DESCENDING) // Neueste zuerst
            .addSnapshotListener { snapshots, e ->
                // Dieser Listener wird bei jeder Änderung in der Datenbank automatisch aufgerufen.

                // Fehlerbehandlung: Wenn etwas schiefgeht, eine Fehlermeldung anzeigen.
                if (e != null) {
                    Toast.makeText(this, "Fehler beim Laden der Gewohnheiten", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                // Wenn die Abfrage erfolgreich war:
                if (snapshots != null) {
                    // Die lokale Liste leeren.
                    habitList.clear()
                    // Alle Dokumente aus dem Ergebnis durchgehen.
                    for (document in snapshots.documents) {
                        // Jedes Dokument in ein Habit-Objekt umwandeln.
                        val habit = document.toObject(Habit::class.java)
                        if (habit != null) {
                            // Die ID des Dokuments im Objekt speichern, damit wir es später wiederfinden.
                            habit.id = document.id
                            // Das Habit-Objekt zur lokalen Liste hinzufügen.
                            habitList.add(habit)
                        }
                    }
                    // Den Adapter mit der neuen, aktualisierten Liste benachrichtigen.
                    habitAdapter.updateData(habitList)
                }
            }
    }

    private fun updateHabitStatus(habit: Habit, isChecked: Boolean) {
        // Den aktuellen Benutzer holen. Wenn niemand angemeldet ist, abbrechen.
        val currentUser = auth.currentUser ?: return

        // Den Pfad zum spezifischen Habit-Dokument in Firestore definieren.
        db.collection("users").document(currentUser.uid).collection("habits").document(habit.id)
            .update("doneToday", isChecked) // Nur das Feld "isDoneToday" aktualisieren.
            .addOnSuccessListener {
                // Optional: Kurze Erfolgsmeldung anzeigen.
                // Toast.makeText(this, "Status aktualisiert", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Fehlerbehandlung: Wenn das Update fehlschlägt, eine Meldung anzeigen.
                Toast.makeText(this, "Fehler beim Aktualisieren", Toast.LENGTH_SHORT).show()
            }
    }

    // Sorgt dafür, dass der Zurück-Pfeil in der ActionBar die Activity schließt.
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
