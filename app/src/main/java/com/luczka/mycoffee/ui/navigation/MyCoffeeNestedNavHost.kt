package com.luczka.mycoffee.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luczka.mycoffee.ui.MyCoffeeViewModelProvider
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsScreen
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsViewModel
import com.luczka.mycoffee.ui.screens.coffees.CoffeesScreen
import com.luczka.mycoffee.ui.screens.coffees.CoffeesViewModel
import com.luczka.mycoffee.ui.screens.history.HistoryScreen
import com.luczka.mycoffee.ui.screens.history.HistoryViewModel
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsScreen
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsViewModel
import com.luczka.mycoffee.ui.screens.home.HomeScreen
import com.luczka.mycoffee.ui.screens.home.HomeViewModel
import com.luczka.mycoffee.ui.screens.methods.MethodsScreen
import com.luczka.mycoffee.ui.screens.methods.MethodsViewModel
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsScreen
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsViewModel
import com.luczka.mycoffee.ui.screens.recipes.RecipesScreen
import com.luczka.mycoffee.ui.screens.recipes.RecipesViewModel

@Composable
fun MyCoffeeNestedNavHost(
    widthSizeClass: WindowWidthSizeClass,
    navController: NavHostController,
    innerPadding: PaddingValues,
    navigateToAssistant: () -> Unit,
    navigateToAddCoffee: () -> Unit,
    navigateToEditCoffee: (Int) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MyCoffeeDestinations.ROUTE_HOME,
        route = "main_branch",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(MyCoffeeDestinations.ROUTE_HOME) {
            val viewModel: HomeViewModel = viewModel(factory = MyCoffeeViewModelProvider.Factory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                navigateToAssistant = navigateToAssistant,
                navigate = navController::navigate,
            )
        }
        composable(MyCoffeeDestinations.ROUTE_HISTORY) {
            val viewModel: HistoryViewModel = viewModel(factory = MyCoffeeViewModelProvider.Factory)
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
            backStackEntry.arguments?.getString(MyCoffeeDestinations.KEY_BREW_ID)?.toIntOrNull()
                ?.let { brewId ->
                    val viewModel: HistoryDetailsViewModel = viewModel(
                        factory = MyCoffeeViewModelProvider.historyDetailsViewModelFactory(brewId = brewId)
                    )
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    HistoryDetailsScreen(
                        historyDetailsUiState = uiState,
                        navigateUp = navController::navigateUp,
                        onDelete = viewModel::onDelete
                    )
                }
        }
        composable(MyCoffeeDestinations.ROUTE_COFFEES) {
            val viewModel: CoffeesViewModel = viewModel(factory = MyCoffeeViewModelProvider.Factory)
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
            backStackEntry.arguments?.getString(MyCoffeeDestinations.KEY_COFFEE_ID)?.toIntOrNull()
                ?.let { coffeeId ->
                    val viewModel: CoffeeDetailsViewModel = viewModel(
                        factory = MyCoffeeViewModelProvider.coffeeDetailsViewModelFactory(
                            coffeeId = coffeeId
                        )
                    )
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
            val viewModel: MethodsViewModel = viewModel(factory = MyCoffeeViewModelProvider.Factory)
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
            val methodId = backStackEntry.arguments?.getString(MyCoffeeDestinations.KEY_METHOD_ID)
                ?: return@composable
            val viewModel: RecipesViewModel = viewModel(
                factory = MyCoffeeViewModelProvider.methodRecipesViewModelFactory(methodId)
            )
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
            backStackEntry.arguments?.getString(MyCoffeeDestinations.KEY_RECIPE_ID)
                ?.let { recipeId ->
                    val viewModel: RecipeDetailsViewModel = viewModel(
                        factory = MyCoffeeViewModelProvider.recipeDetailsViewModelFactory(
                            recipeId = recipeId
                        )
                    )
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