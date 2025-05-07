package com.shak.shaknotes.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shak.shaknotes.database.entities.Note

@Dao
interface NoteDao {

    // Insert a single note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    // Update an existing note
    @Update
    fun updateNote(note: Note)

    // Delete a note
    @Delete
    fun deleteNote(note: Note)

    // Get all notes as a Flow (for live data)
    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllNotes(): List<Note>

    // Get a note by ID
    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getNoteById(noteId: Int): Note?
}
