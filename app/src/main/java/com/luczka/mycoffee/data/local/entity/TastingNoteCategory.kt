package com.luczka.mycoffee.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TastingNoteCategory(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long = 0,
    val name: Int
) {
    companion object {
        const val KEY_COLUMN = "categoryId"
    }
}