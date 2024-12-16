package com.luczka.mycoffee.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasting_note",
    foreignKeys = [
        ForeignKey(
            entity = TastingNoteCategory::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TastingNote::class,
            parentColumns = ["noteId"],
            childColumns = ["parentNoteId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TastingNote(
    @PrimaryKey(autoGenerate = true) val noteId: Long = 0,
    val name: String,
    val colorHex: String,
    @ColumnInfo(index = true) val categoryId: Long, // Foreign key to TastingNoteCategory
    val parentNoteId: Long? = null // Foreign key to self for hierarchical notes
)