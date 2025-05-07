package com.shak.shaknotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.shak.shaknotes.R
import com.shak.shaknotes.database.dbHelpers.NoteDbHelper
import com.shak.shaknotes.database.entities.Note


class NoteRvAdapter(private val notesList: List<Note>) :
    RecyclerView.Adapter<NoteRvAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.note_item_lay, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.title.text = note.title
        holder.noteContent.text = note.noteContent

        holder.delBtn.setOnClickListener {
            val noteDbHelper: NoteDbHelper = NoteDbHelper.getInstance(holder.itemView.context)
            noteDbHelper.noteDao().deleteNote(note)
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val delBtn: AppCompatButton = itemView.findViewById<AppCompatButton>(R.id.deleteBtn)
        val title: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.titleTxt)
        val noteContent: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.noteContentTxt)
    }

}