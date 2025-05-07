package com.shak.shaknotes.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class Note (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "noteContent")
    var noteContent: String = ""
)