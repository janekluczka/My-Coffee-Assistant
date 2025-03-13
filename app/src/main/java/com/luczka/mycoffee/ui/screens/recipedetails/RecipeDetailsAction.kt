package com.luczka.mycoffee.ui.screens.recipedetails

sealed class RecipeDetailsAction {
    data object NavigateUp : RecipeDetailsAction()
    data object ShowLeaveApplicationDialog : RecipeDetailsAction()
    data object OnLeaveApplicationClicked : RecipeDetailsAction()
}