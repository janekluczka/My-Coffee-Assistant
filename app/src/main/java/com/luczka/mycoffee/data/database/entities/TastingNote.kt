package com.luczka.mycoffee.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TastingNoteCategory::class,
            parentColumns = [TastingNoteCategory.KEY_COLUMN],
            childColumns = [TastingNote.CATEGORY_KEY_COLUMN],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TastingNote::class,
            parentColumns = [TastingNote.KEY_COLUMN],
            childColumns = [TastingNote.PARENT_NOTE_KEY_COLUMN],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TastingNote(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0,
    val name: Int,
    val colorHex: String,
    @ColumnInfo(index = true)
    val categoryId: Long,
    val parentNoteId: Long? = null
) {
    companion object {
        const val KEY_COLUMN = "noteId"
        const val PARENT_NOTE_KEY_COLUMN = "parentNoteId"
        const val CATEGORY_KEY_COLUMN = "categoryId"
    }
}