package com.luczka.mycoffee.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MethodUiState(
    val id: String = "",
    val name: String = "",
    val description: String = "",
) : Parcelable