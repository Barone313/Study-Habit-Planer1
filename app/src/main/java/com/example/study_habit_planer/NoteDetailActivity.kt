package com.example.study_habit_planer

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var noteId: String? = null

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notiz bearbeiten"

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        noteId = intent.getStringExtra("noteId")

        titleEditText = findViewById(R.id.editTextNoteTitle)
        contentEditText = findViewById(R.id.editTextNoteContent)
        val saveButton = findViewById<Button>(R.id.buttonSaveNote)

        if (noteId != null) {
            supportActionBar?.title = "Notiz bearbeiten"
            loadNoteData()
        } else {
            supportActionBar?.title = "Neue Notiz erstellen"
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() || content.isNotEmpty()) {
                saveNote(title, content)

            } else {
                Toast.makeText(this, "Titel oder Inhalt dürfen nicht leer sein", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadNoteData() {
        val currentUser = auth.currentUser ?: return
        noteId?.let {
            db.collection("users").document(currentUser.uid).collection("notes").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val note = document.toObject(Note::class.java)
                        note?.let {
                            titleEditText.setText(it.title)
                            contentEditText.setText(it.content)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Fehler beim Laden der Notiz: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun saveNote(title: String, content: String) {
        val currentUser = auth.currentUser ?: return
        val collection = db.collection("users").document(currentUser.uid).collection("notes")

        if (noteId == null) {
            val newNote = Note(
                title = title,
                content = content,
                createdAt = System.currentTimeMillis()


            )
            collection.add(newNote)
                .addOnSuccessListener { 
                    finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Fehler beim Speichern: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            val noteUpdates = mapOf(
                "title" to title,
                "content" to content
            )
            collection.document(noteId!!).update(noteUpdates)
                .addOnSuccessListener { 
                    finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Fehler beim Speichern: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}