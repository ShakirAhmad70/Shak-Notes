package com.shak.shaknotes.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.shak.shaknotes.R
import com.shak.shaknotes.database.dbHelpers.NoteDbHelper
import com.shak.shaknotes.database.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteRvAdapter(private var notesList: MutableList<Note>) :
    RecyclerView.Adapter<NoteRvAdapter.NoteViewHolder>() {

    // Callback to notify data changes (used by fragment to show/hide empty layout)
    var onDataChanged: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item_lay, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.title.text = note.title
        holder.noteContent.text = note.noteContent

        // Delete Note
        holder.delBtn.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes") { _, _ ->
                    val noteDbHelper = NoteDbHelper.getInstance(holder.itemView.context)

                    CoroutineScope(Dispatchers.IO).launch {
                        noteDbHelper.noteDao().deleteNote(note)

                        withContext(Dispatchers.Main) {
                            notesList.removeAt(holder.adapterPosition)
                            notifyItemRemoved(holder.adapterPosition)
                            onDataChanged?.invoke()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Edit Note
        holder.noteItemCard.setOnClickListener {
            val dialog = Dialog(holder.itemView.context)
            dialog.setContentView(R.layout.add_note_dialog)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val titleEdt = dialog.findViewById<AppCompatEditText>(R.id.titleEdt)
            val noteEdt = dialog.findViewById<AppCompatEditText>(R.id.noteEdt)
            val addBtn = dialog.findViewById<AppCompatButton>(R.id.addBtn)

            titleEdt.setText(note.title)
            noteEdt.setText(note.noteContent)
            addBtn.text = holder.itemView.context.getString(R.string.update_note)

            dialog.show()

            val noteDbHelper = NoteDbHelper.getInstance(holder.itemView.context)

            addBtn.setOnClickListener {
                val updatedTitle = titleEdt.text.toString().trim()
                val updatedNoteContent = noteEdt.text.toString().trim()

                if (updatedTitle.isNotEmpty() && updatedNoteContent.isNotEmpty()) {
                    val updatedNote = Note(note.id, updatedTitle, updatedNoteContent)

                    CoroutineScope(Dispatchers.IO).launch {
                        noteDbHelper.noteDao().updateNote(updatedNote)

                        withContext(Dispatchers.Main) {
                            notesList[holder.adapterPosition] = updatedNote
                            notifyItemChanged(holder.adapterPosition)
                            onDataChanged?.invoke()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = notesList.size

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val delBtn: AppCompatButton = itemView.findViewById(R.id.deleteBtn)
        val title: AppCompatTextView = itemView.findViewById(R.id.titleTxt)
        val noteContent: AppCompatTextView = itemView.findViewById(R.id.noteContentTxt)
        val noteItemCard: View = itemView.findViewById(R.id.noteItemCard)
    }
}