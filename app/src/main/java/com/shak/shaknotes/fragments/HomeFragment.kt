package com.shak.shaknotes.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shak.shaknotes.R
import com.shak.shaknotes.adapters.NoteRvAdapter
import com.shak.shaknotes.database.dbHelpers.NoteDbHelper
import com.shak.shaknotes.database.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var showItemsRV: RecyclerView
    private lateinit var showEmptyLay: LinearLayoutCompat
    private lateinit var noteAdapter: NoteRvAdapter
    private val noteDbHelper by lazy { NoteDbHelper.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val addNoteBtn = view.findViewById<AppCompatButton>(R.id.addNoteBtn)
        val addItemFab = view.findViewById<FloatingActionButton>(R.id.addItemFab)
        showItemsRV = view.findViewById(R.id.showItemsRV)
        showEmptyLay = view.findViewById(R.id.showEmptyLay)

        showItemsRV.layoutManager = GridLayoutManager(requireContext(), 2)

        // Load notes initially
        loadNotes()

        // Add new note dialog
        addItemFab.setOnClickListener {
            showAddNoteDialog()
        }

        addNoteBtn.setOnClickListener {
            addItemFab.performClick()
        }

        return view
    }

    private fun loadNotes() {
        lifecycleScope.launch(Dispatchers.IO) {
            val notesList = noteDbHelper.noteDao().getAllNotes().toMutableList()

            withContext(Dispatchers.Main) {
                noteAdapter = NoteRvAdapter(notesList)
                showItemsRV.adapter = noteAdapter

                noteAdapter.onDataChanged = {
                    toggleEmptyState(noteAdapter.itemCount == 0)
                }

                toggleEmptyState(notesList.isEmpty())
            }
        }
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            showEmptyLay.visibility = View.VISIBLE
            showItemsRV.visibility = View.GONE
        } else {
            showEmptyLay.visibility = View.GONE
            showItemsRV.visibility = View.VISIBLE
        }
    }

    private fun showAddNoteDialog() {
        val addNoteDialog = Dialog(requireContext())
        addNoteDialog.setContentView(R.layout.add_note_dialog)
        addNoteDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val edtTitle = addNoteDialog.findViewById<AppCompatEditText>(R.id.titleEdt)
        val edtNote = addNoteDialog.findViewById<AppCompatEditText>(R.id.noteEdt)
        val addBtn = addNoteDialog.findViewById<AppCompatButton>(R.id.addBtn)

        addNoteDialog.show()

        addBtn.setOnClickListener {
            val title = edtTitle.text.toString().trim()
            val noteContent = edtNote.text.toString().trim()

            if (title.isNotEmpty() && noteContent.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val newNote = Note(title = title, noteContent = noteContent)
                    noteDbHelper.noteDao().insertNote(newNote)
                    val updatedNotes = noteDbHelper.noteDao().getAllNotes().toMutableList()

                    withContext(Dispatchers.Main) {
                        noteAdapter = NoteRvAdapter(updatedNotes)
                        noteAdapter.onDataChanged = {
                            toggleEmptyState(noteAdapter.itemCount == 0)
                        }
                        showItemsRV.adapter = noteAdapter
                        toggleEmptyState(updatedNotes.isEmpty())
                        addNoteDialog.dismiss()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter title and note", Toast.LENGTH_SHORT).show()
            }
        }
    }
}