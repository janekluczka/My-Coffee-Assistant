package com.luczka.mycoffee.ui.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.luczka.mycoffee.ui.models.MethodUiState
import com.luczka.mycoffee.ui.models.RecipeUiState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavTypes {

    val MethodUiStateNavType = object : NavType<MethodUiState>(isNullableAllowed = false) {
        override fun put(bundle: Bundle, key: String, value: MethodUiState) {
            bundle.putParcelable(key, value)
        }

        override fun get(bundle: Bundle, key: String): MethodUiState {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, MethodUiState::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(key)!!
            }
        }

        override fun serializeAsValue(value: MethodUiState): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun parseValue(value: String): MethodUiState {
            return Json.decodeFromString<MethodUiState>(value)
        }
    }

    val RecipeUiStateNavType = object : NavType<RecipeUiState>(isNullableAllowed = false) {
        override fun put(bundle: Bundle, key: String, value: RecipeUiState) {
            bundle.putParcelable(key, value)
        }

        override fun get(bundle: Bundle, key: String): RecipeUiState {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, RecipeUiState::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(key)!!
            }
        }

        override fun serializeAsValue(value: RecipeUiState): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun parseValue(value: String): RecipeUiState {
            return Json.decodeFromString<RecipeUiState>(value)
        }
    }

}