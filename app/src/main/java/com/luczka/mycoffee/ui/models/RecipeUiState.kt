package com.luczka.mycoffee.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class RecipeUiState(
    val youtubeId: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val author: String = "",
    val title: String = "",
    val steps: List<BrewingStepUiState> = emptyList(),
) : Parcelable