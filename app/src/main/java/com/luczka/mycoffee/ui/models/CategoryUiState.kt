package com.luczka.mycoffee.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CategoryUiState(
    val id: String = "",
    val name: String = "",
    val description: String = "",
) : Parcelable, Comparable<CategoryUiState> {

    override fun compareTo(other: CategoryUiState): Int {
        return name.compareTo(other.name)
    }
}