package com.coffee.mycoffeeassistant.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@Composable
fun rememberMyCoffeeAssistantAppState(
    navController: NavHostController = rememberNavController(),
) = remember(navController) {
    MyCoffeeAssistantAppState(navController)
}

@Stable
class MyCoffeeAssistantAppState(
    val navController: NavHostController
) {
    val navigationBarDestinations: List<Screen> = listOf(
        Screen.Home,
        Screen.Cupboard,
        Screen.Recipes
    )
    var onAddToFavouritesAction: () -> Unit = {}
    var onEditAction: () -> Unit = {}

}