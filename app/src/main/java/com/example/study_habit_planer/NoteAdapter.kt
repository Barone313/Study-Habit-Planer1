package com.example.study_habit_planer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val notes: List<Note>,
    private val onEditClickListener: (Note) -> Unit,
    private val onDeleteClickListener: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.editButton.setOnClickListener {
            onEditClickListener(note)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClickListener(note)
        }
    }

    override fun getItemCount() = notes.size

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewNoteTitle)
        val contentTextView: TextView = itemView.findViewById(R.id.textViewNoteContent)
        val editButton: ImageButton = itemView.findViewById(R.id.buttonEditNote)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteNote)
    }
}