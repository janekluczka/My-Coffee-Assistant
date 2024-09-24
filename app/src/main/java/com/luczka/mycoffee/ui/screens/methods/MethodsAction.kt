package com.luczka.mycoffee.ui.screens.methods

sealed class MethodsAction {
    data class NavigateToRecipes(val methodId: String) : MethodsAction()
}