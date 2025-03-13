package com.luczka.mycoffee.ui.screens.recipedetails

sealed class RecipeDetailsOneTimeEvent {
    data object NavigateUp : RecipeDetailsOneTimeEvent()
    data class OpenBrowser(val uri: String) : RecipeDetailsOneTimeEvent()
}