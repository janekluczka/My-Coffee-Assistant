package com.luczka.mycoffee.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsAction
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsScreen
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsViewModel
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsViewModelFactory
import com.luczka.mycoffee.ui.screens.coffees.CoffeesAction
import com.luczka.mycoffee.ui.screens.coffees.CoffeesScreen
import com.luczka.mycoffee.ui.screens.coffees.CoffeesViewModel
import com.luczka.mycoffee.ui.screens.history.HistoryAction
import com.luczka.mycoffee.ui.screens.history.HistoryScreen
import com.luczka.mycoffee.ui.screens.history.HistoryViewModel
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsScreen
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsViewModel
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsViewModelFactory
import com.luczka.mycoffee.ui.screens.home.HomeAction
import com.luczka.mycoffee.ui.screens.home.HomeScreen
import com.luczka.mycoffee.ui.screens.home.HomeViewModel
import com.luczka.mycoffee.ui.screens.methods.MethodsAction
import com.luczka.mycoffee.ui.screens.methods.MethodsScreen
import com.luczka.mycoffee.ui.screens.methods.MethodsViewModel
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsAction
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsScreen
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsViewModel
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsViewModelFactory
import com.luczka.mycoffee.ui.screens.recipes.RecipesAction
import com.luczka.mycoffee.ui.screens.recipes.RecipesScreen
import com.luczka.mycoffee.ui.screens.recipes.RecipesViewModel
import com.luczka.mycoffee.ui.screens.recipes.RecipesViewModelFactory

@Composable
fun MyCoffeeNestedNavHost(
    widthSizeClass: WindowWidthSizeClass,
    modifier: Modifier,
    navController: NavHostController,
    navigateToAssistant: () -> Unit,
    navigateToAddCoffee: () -> Unit,
    navigateToEditCoffee: (Int) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MyCoffeeDestinations.ROUTE_HOME,
        route = "main_branch",
    ) {
        composable(MyCoffeeDestinations.ROUTE_HOME) {
            val viewModel = hiltViewModel<HomeViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        HomeAction.NavigateToAssistant -> navigateToAssistant()
                    }
                }
            )
        }
        composable(MyCoffeeDestinations.ROUTE_HISTORY) {
            val viewModel = hiltViewModel<HistoryViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HistoryScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is HistoryAction.NavigateToHistoryDetails -> navController.navigateToHistoryDetails(action.brewId)
                    }
                }
            )
        }
        composable(
            route = "${MyCoffeeDestinations.ROUTE_HISTORY}/{${MyCoffeeDestinations.KEY_BREW_ID}}",
            arguments = listOf(
                navArgument(
                    name = MyCoffeeDestinations.KEY_BREW_ID,
                    builder = { type = NavType.StringType }
                )
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(MyCoffeeDestinations.KEY_BREW_ID)?.toIntOrNull()?.let { brewId ->
                val viewModel = hiltViewModel<HistoryDetailsViewModel, HistoryDetailsViewModelFactory> { factory ->
                    factory.create(brewId)
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                HistoryDetailsScreen(
                    historyDetailsUiState = uiState,
                    navigateUp = navController::navigateUp,
                    onDelete = viewModel::delete
                )
            }
        }
        composable(MyCoffeeDestinations.ROUTE_COFFEES) {
            val viewModel = hiltViewModel<CoffeesViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CoffeesScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        CoffeesAction.NavigateUp -> navController.navigateUp()
                        CoffeesAction.NavigateToAddCoffee -> navigateToAddCoffee()
                        is CoffeesAction.NavigateToCoffeeDetails -> navController.navigateToCoffeeDetails(action.coffeeId)
                        else -> {}
                    }
                    viewModel.onAction(action)
                },
            )
        }
        composable(
            route = "${MyCoffeeDestinations.ROUTE_COFFEES}/{${MyCoffeeDestinations.KEY_COFFEE_ID}}",
            arguments = listOf(
                navArgument(
                    name = MyCoffeeDestinations.KEY_COFFEE_ID,
                    builder = { type = NavType.StringType }
                )
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(MyCoffeeDestinations.KEY_COFFEE_ID)?.toIntOrNull()?.let { coffeeId ->
                val viewModel = hiltViewModel<CoffeeDetailsViewModel, CoffeeDetailsViewModelFactory> { factory ->
                    factory.create(coffeeId)
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                CoffeeDetailsScreen(
                    widthSizeClass = widthSizeClass,
                    uiState = uiState,
                    onAction = { action ->
                        when (action) {
                            is CoffeeDetailsAction.NavigateUp -> navController.navigateUp()
                            is CoffeeDetailsAction.OnEditClicked -> navigateToEditCoffee(action.coffeeId)
                            else -> {}
                        }
                        viewModel.onAction(action)
                    }
                )
            }
        }
        composable(MyCoffeeDestinations.ROUTE_RECIPES) {
            val viewModel = hiltViewModel<MethodsViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MethodsScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is MethodsAction.NavigateToRecipes -> navController.navigateToRecipes(action.methodId)
                    }
                }
            )
        }
        composable(
            route = "${MyCoffeeDestinations.ROUTE_RECIPES}/{${MyCoffeeDestinations.KEY_METHOD_ID}}",
            arguments = listOf(
                navArgument(
                    name = MyCoffeeDestinations.KEY_METHOD_ID,
                    builder = { type = NavType.StringType }
                )
            )
        ) { backStackEntry ->
            val methodId = backStackEntry.arguments?.getString(MyCoffeeDestinations.KEY_METHOD_ID) ?: return@composable
            val viewModel = hiltViewModel<RecipesViewModel, RecipesViewModelFactory> { factory ->
                factory.create(methodId)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RecipesScreen(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is RecipesAction.NavigateUp -> navController.navigateUp()
                        is RecipesAction.NavigateToRecipeDetails -> navController.navigateToRecipeDetails(methodId = action.methodId, recipeId = action.recipeId)
                    }
                },
            )
        }
        composable(
            route = "${MyCoffeeDestinations.ROUTE_RECIPES}/{${MyCoffeeDestinations.KEY_METHOD_ID}}/{${MyCoffeeDestinations.KEY_RECIPE_ID}}",
            arguments = listOf(
                navArgument(
                    name = MyCoffeeDestinations.KEY_METHOD_ID,
                    builder = { type = NavType.StringType }
                ),
                navArgument(
                    name = MyCoffeeDestinations.KEY_RECIPE_ID,
                    builder = { type = NavType.StringType }
                )
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(MyCoffeeDestinations.KEY_RECIPE_ID)?.let { recipeId ->
                val viewModel = hiltViewModel<RecipeDetailsViewModel, RecipeDetailsViewModelFactory> { factory ->
                    factory.create(recipeId)
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                RecipeDetailsScreen(
                    widthSizeClass = widthSizeClass,
                    uiState = uiState,
                    onAction = { action ->
                        when(action) {
                            RecipeDetailsAction.NavigateUp -> navController.navigateUp()
                        }
                    }
                )
            }
        }
    }
}

private fun NavHostController.navigateToHistoryDetails(brewId: Int) {
    this.navigate("${MyCoffeeDestinations.ROUTE_HISTORY}/${brewId}")
}

private fun NavHostController.navigateToCoffeeDetails(coffeeId: Int) {
    this.navigate("${MyCoffeeDestinations.ROUTE_COFFEES}/${coffeeId}")
}

private fun NavHostController.navigateToRecipes(methodId: String) {
    this.navigate("${MyCoffeeDestinations.ROUTE_RECIPES}/${methodId}")
}

private fun NavHostController.navigateToRecipeDetails(methodId: String, recipeId: String) {
    this.navigate("${MyCoffeeDestinations.ROUTE_RECIPES}/${methodId}/${recipeId}")
}