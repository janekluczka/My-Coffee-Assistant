package com.coffee.mycoffeeassistant.ui.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.MyCoffeeAssistantAppState
import com.coffee.mycoffeeassistant.ui.screens.methodrecipes.MethodRecipesScreen
import com.coffee.mycoffeeassistant.ui.screens.methodrecipes.MethodRecipesViewModel
import com.coffee.mycoffeeassistant.ui.screens.methods.MethodsScreen
import com.coffee.mycoffeeassistant.ui.screens.methods.MethodsViewModel
import com.coffee.mycoffeeassistant.ui.screens.recipedetails.RecipeDetailsScreen
import com.coffee.mycoffeeassistant.ui.screens.recipedetails.RecipeDetailsViewModel

fun NavGraphBuilder.recipesNavGraph(appState: MyCoffeeAssistantAppState) {
    navigation(
        startDestination = Screen.Recipes.route,
        route = "recipes"
    ) {
        composable(Screen.Recipes.route) {
            val methodsViewModel: MethodsViewModel =
                viewModel(factory = AppViewModelProvider.Factory)
            val methodsUiState by methodsViewModel.uiState.collectAsStateWithLifecycle()

            MethodsScreen(
                methodsUiState = methodsUiState,
                navigate = appState.navController::navigate,
            )
        }
        composable(
            Screen.MethodRecipes.route + "/{methodId}",
            arguments = listOf(navArgument("methodId") { type = NavType.StringType })
        ) { backStackEntry ->
            val methodId = backStackEntry.arguments?.getString("methodId")
            if (methodId != null) {
                val factory = AppViewModelProvider.methodRecipesViewModelFactory(methodId = methodId)
                val methodRecipesViewModel: MethodRecipesViewModel = viewModel(factory = factory)
                val methodRecipesUiState by methodRecipesViewModel.uiState.collectAsStateWithLifecycle()

                MethodRecipesScreen(
                    methodRecipesUiState = methodRecipesUiState,
                    navigateUp = appState.navController::navigateUp,
                    navigate = appState.navController::navigate
                )
            }
        }
        composable(
            Screen.RecipeDetails.route + "/{youtubeId}",
            arguments = listOf(navArgument("youtubeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val youtubeId = backStackEntry.arguments?.getString("youtubeId")
            if (youtubeId != null) {
                val factory =
                    AppViewModelProvider.recipeDetailsViewModelFactory(youtubeId = youtubeId)
                val recipeDetailsViewModel: RecipeDetailsViewModel = viewModel(factory = factory)
                val recipeDetailsUiState by recipeDetailsViewModel.uiState.collectAsStateWithLifecycle()

                RecipeDetailsScreen(
                    recipeDetailsUiState = recipeDetailsUiState,
                    navigateUp = appState.navController::navigateUp
                )
            }
        }
    }
}