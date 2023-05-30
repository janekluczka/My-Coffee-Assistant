package com.coffee.mycoffeeassistant.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.coffee.mycoffeeassistant.ui.MyCoffeeAssistantAppState
import com.coffee.mycoffeeassistant.ui.screens.methodrecipes.MethodRecipesScreen
import com.coffee.mycoffeeassistant.ui.screens.recipedetails.RecipeDetailsScreen
import com.coffee.mycoffeeassistant.ui.screens.methods.MethodsScreen

fun NavGraphBuilder.recipesNavGraph(appState: MyCoffeeAssistantAppState) {
    navigation(
        startDestination = Screen.Recipes.route,
        route = "recipes"
    ) {
        composable(Screen.Recipes.route) {
            MethodsScreen(navigateToMethodRecipes = {
                appState.navController.navigate(Screen.MethodRecipes.createRoute(it))
            })
        }
        composable(
            Screen.MethodRecipes.route + "/{methodId}",
            arguments = listOf(navArgument("methodId") { type = NavType.StringType })
        ) { backStackEntry ->
            val methodId = backStackEntry.arguments?.getString("methodId")
            if (methodId != null) {
                MethodRecipesScreen(
                    methodId = methodId,
                    navigateToRecipeDetails = {
                        appState.navController.navigate(Screen.RecipeDetails.createRoute(it))
                    }
                )
            }
        }
        composable(
            Screen.RecipeDetails.route + "/{youtubeId}",
            arguments = listOf(navArgument("youtubeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val youtubeId = backStackEntry.arguments?.getString("youtubeId")
            if (youtubeId != null) {
                RecipeDetailsScreen(youtubeId = youtubeId)
            }
        }
    }
}