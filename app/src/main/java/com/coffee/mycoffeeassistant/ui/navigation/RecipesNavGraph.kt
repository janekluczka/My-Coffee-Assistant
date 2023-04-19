package com.coffee.mycoffeeassistant.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.coffee.mycoffeeassistant.ui.MyCoffeeAssistantAppState
import com.coffee.mycoffeeassistant.ui.components.UserGreeting
import com.coffee.mycoffeeassistant.ui.screens.MethodRecipesScreen
import com.coffee.mycoffeeassistant.ui.screens.RecipesScreen

fun NavGraphBuilder.recipesNavGraph(appState: MyCoffeeAssistantAppState) {
    navigation(
        startDestination = Screen.Recipes.route,
        route = "recipes"
    ) {
        composable(Screen.Recipes.route) {
            RecipesScreen(navController = appState.navController)
        }
        composable(Screen.MethodRecipes.route) {
            MethodRecipesScreen(navController = appState.navController)
        }
        composable(Screen.RecipeDetails.route) {
            UserGreeting("Recipe")
        }
    }
}