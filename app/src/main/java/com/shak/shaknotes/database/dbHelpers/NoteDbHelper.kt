package com.shak.shaknotes.database.dbHelpers

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shak.shaknotes.database.daos.NoteDao
import com.shak.shaknotes.database.entities.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDbHelper : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: NoteDbHelper? = null
        private const val DB_NAME = "notes.db"

        @Synchronized
        fun getInstance(context: Context): NoteDbHelper {
            return instance ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDbHelper::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = tempInstance
                tempInstance
            }
        }
    }
}