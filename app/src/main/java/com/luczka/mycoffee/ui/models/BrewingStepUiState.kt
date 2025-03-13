package com.luczka.mycoffee.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class BrewingStepUiState(
    val number: Int,
    val description: String,
    val time: String?,
) : Parcelable