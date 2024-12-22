package com.luczka.mycoffee.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasting_note_category")
data class TastingNoteCategory(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long = 0,
    val name: String
)