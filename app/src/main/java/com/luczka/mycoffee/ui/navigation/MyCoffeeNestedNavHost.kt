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
import com.luczka.mycoffee.ui.screen.coffeedetails.CoffeeDetailsScreen
import com.luczka.mycoffee.ui.screen.coffeedetails.CoffeeDetailsViewModel
import com.luczka.mycoffee.ui.screen.coffeedetails.CoffeeDetailsViewModelFactory
import com.luczka.mycoffee.ui.screen.coffees.CoffeesScreen
import com.luczka.mycoffee.ui.screen.coffees.CoffeesViewModel
import com.luczka.mycoffee.ui.screen.history.HistoryScreen
import com.luczka.mycoffee.ui.screen.history.HistoryViewModel
import com.luczka.mycoffee.ui.screen.historydetails.HistoryDetailsScreen
import com.luczka.mycoffee.ui.screen.historydetails.HistoryDetailsViewModel
import com.luczka.mycoffee.ui.screen.historydetails.HistoryDetailsViewModelFactory
import com.luczka.mycoffee.ui.screen.home.HomeScreen
import com.luczka.mycoffee.ui.screen.home.HomeViewModel
import com.luczka.mycoffee.ui.screen.methods.MethodsScreen
import com.luczka.mycoffee.ui.screen.methods.MethodsViewModel
import com.luczka.mycoffee.ui.screen.recipedetails.RecipeDetailsScreen
import com.luczka.mycoffee.ui.screen.recipedetails.RecipeDetailsViewModel
import com.luczka.mycoffee.ui.screen.recipedetails.RecipeDetailsViewModelFactory
import com.luczka.mycoffee.ui.screen.recipes.RecipesScreen
import com.luczka.mycoffee.ui.screen.recipes.RecipesViewModel
import com.luczka.mycoffee.ui.screen.recipes.RecipesViewModelFactory

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
                navigateToAssistant = navigateToAssistant,
                navigate = navController::navigate,
            )
        }
        composable(MyCoffeeDestinations.ROUTE_HISTORY) {
            val viewModel = hiltViewModel<HistoryViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HistoryScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onListItemClicked = navController::navigateToBrewDetails
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
                    onDelete = viewModel::onDelete
                )
            }
        }
        composable(MyCoffeeDestinations.ROUTE_COFFEES) {
            val viewModel = hiltViewModel<CoffeesViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CoffeesScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                navigateUp = navController::navigateUp,
                navigateToAddCoffee = navigateToAddCoffee,
                onSelectFilter = viewModel::onFilterSelected,
                onCoffeeSelected = navController::navigateToCoffeeDetails
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
                    navigateUp = navController::navigateUp,
                    onUpdateFavourite = viewModel::onUpdateFavourite,
                    onEdit = navigateToEditCoffee,
                    onDelete = viewModel::onDelete
                )
            }
        }
        composable(MyCoffeeDestinations.ROUTE_RECIPES) {
            val viewModel = hiltViewModel<MethodsViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MethodsScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                navigateToRecipes = navController::navigateToRecipes
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
            val viewModel = hiltViewModel<RecipesViewModel, RecipesViewModelFactory> {
                it.create(methodId)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RecipesScreen(
                uiState = uiState,
                navigateUp = navController::navigateUp,
                onRecipeSelected = { youtubeId ->
                    navController.navigateToRecipeDetails(
                        methodId = methodId,
                        recipeId = youtubeId
                    )
                }
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
                    navigateUp = navController::navigateUp
                )
            }
        }
    }
}

private fun NavHostController.navigateToBrewDetails(brewId: Int) {
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