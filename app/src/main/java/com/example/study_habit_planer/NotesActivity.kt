package com.example.study_habit_planer

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NotesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var noteAdapter: NoteAdapter
    private val notesList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Meine Notizen"

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val notesRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotes)
        val addNoteFab = findViewById<FloatingActionButton>(R.id.fabAddNote)

        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        
        noteAdapter = NoteAdapter(notesList, { note -> editNote(note) }, { note -> deleteNote(note) })
        notesRecyclerView.adapter = noteAdapter

        addNoteFab.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java)
            startActivity(intent)
        }

        loadNotes()
    }

    private fun loadNotes() {
        val currentUser = auth.currentUser ?: return

        db.collection("users").document(currentUser.uid).collection("notes")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Fehler beim Laden der Notizen: ${e.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                notesList.clear()
                snapshots?.mapNotNull { it.toObject(Note::class.java).apply { id = it.id } }?.let { notesList.addAll(it) }
                if(::noteAdapter.isInitialized) {
                   noteAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun editNote(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
    }

    private fun deleteNote(note: Note) {
        val currentUser = auth.currentUser
        if (currentUser == null || note.id.isEmpty()) {
            Toast.makeText(this, "Fehler: Notiz konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users").document(currentUser.uid).collection("notes").document(note.id)
            .delete()
            .addOnSuccessListener { Toast.makeText(this, "Notiz gelöscht", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(this, "Fehler beim Löschen: ${e.message}", Toast.LENGTH_LONG).show() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}