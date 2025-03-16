package com.luczka.mycoffee.ui.screens.recipedetails

sealed class RecipeDetailsAction {
    data object NavigateUp : RecipeDetailsAction()
    data object ShowOpenYouTubeDialog : RecipeDetailsAction()
    data object HideOpenYouTubeDialog : RecipeDetailsAction()
    data object OnLeaveApplicationClicked : RecipeDetailsAction()
}