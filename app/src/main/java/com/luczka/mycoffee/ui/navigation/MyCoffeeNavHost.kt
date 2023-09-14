package com.luczka.mycoffee.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import com.luczka.mycoffee.ui.screens.home.HomeScreen
import com.luczka.mycoffee.ui.screens.home.HomeViewModel
import com.luczka.mycoffee.ui.screens.methods.MethodsScreen
import com.luczka.mycoffee.ui.screens.methods.MethodsViewModel
import com.luczka.mycoffee.ui.screens.mybags.CoffeeDetailsScreen
import com.luczka.mycoffee.ui.screens.mybags.MyBagsScreen
import com.luczka.mycoffee.ui.screens.mybags.MyBagsViewModel
import com.luczka.mycoffee.ui.screens.recipes.RecipeDetailsScreen
import com.luczka.mycoffee.ui.screens.recipes.RecipesScreen
import com.luczka.mycoffee.ui.screens.recipes.RecipesViewModel

@Composable
fun MyCoffeeNavHost(
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
                uiState = uiState,
                navigateToAssistant = navigateToAssistant,
                navigate = navController::navigate,
            )
        }
        composable(MyCoffeeDestinations.ROUTE_MY_BAGS) {
            val viewModel: MyBagsViewModel = viewModel(factory = MyCoffeeViewModelProvider.Factory)

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    Crossfade(
                        targetState = uiState.showDetails,
                        label = ""
                    ) { showDetails ->
                        if (!showDetails) {
                            MyBagsScreen(
                                widthSizeClass = widthSizeClass,
                                uiState = uiState,
                                navigateUp = navController::navigateUp,
                                navigateToAddCoffee = navigateToAddCoffee,
                                onSelectFilter = viewModel::onFilterSelected,
                                onCoffeeSelected = { coffeeId ->
                                    viewModel.onCoffeeSelected(
                                        coffeeId = coffeeId,
                                        showDetails = true
                                    )
                                }
                            )
                        } else {
                            BackHandler {
                                viewModel.onHideDetails()
                            }

                            CoffeeDetailsScreen(
                                widthSizeClass = widthSizeClass,
                                coffeeUiState = uiState.selectedCoffee,
                                navigateUp = navController::navigateUp,
                                onUpdateFavourite = viewModel::onUpdateFavourite,
                                onEdit = navigateToEditCoffee,
                                onDelete = viewModel::onDelete
                            )
                        }
                    }
                }

                else -> {
                    Row {
                        Box(modifier = Modifier.weight(2f)) {
                            MyBagsScreen(
                                widthSizeClass = widthSizeClass,
                                uiState = uiState,
                                navigateUp = navController::navigateUp,
                                navigateToAddCoffee = navigateToAddCoffee,
                                onSelectFilter = viewModel::onFilterSelected,
                                onCoffeeSelected = { coffeeId ->
                                    viewModel.onCoffeeSelected(
                                        coffeeId = coffeeId,
                                        showDetails = false
                                    )
                                }
                            )
                        }
                        // TODO: Add crossfade when changing details
                        Box(modifier = Modifier.weight(3f)) {
                            CoffeeDetailsScreen(
                                widthSizeClass = widthSizeClass,
                                coffeeUiState = uiState.selectedCoffee,
                                navigateUp = navController::navigateUp,
                                onUpdateFavourite = viewModel::onUpdateFavourite,
                                onEdit = navigateToEditCoffee,
                                onDelete = viewModel::onDelete
                            )
                        }
                    }
                }
            }
        }
        composable(MyCoffeeDestinations.ROUTE_RECIPES) {
            val viewModel: MethodsViewModel = viewModel(factory = MyCoffeeViewModelProvider.Factory)

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MethodsScreen(
                uiState = uiState,
                navigateToRecipes = { methodId ->
                    val route = "${MyCoffeeDestinations.ROUTE_RECIPES}/${methodId}"
                    navController.navigate(route)
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
            val methodId = backStackEntry.arguments?.getString("methodId") ?: return@composable

            val viewModel: RecipesViewModel = viewModel(
                factory = MyCoffeeViewModelProvider.methodRecipesViewModelFactory(methodId)
            )

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    Crossfade(
                        targetState = uiState.showDetails,
                        label = ""
                    ) { showDetails ->
                        if (!showDetails) {
                            RecipesScreen(
                                uiState = uiState,
                                navigateUp = navController::navigateUp,
                                onRecipeSelected = { youtubeId ->
                                    viewModel.selectRecipe(
                                        youtubeId = youtubeId,
                                        showDetails = true
                                    )
                                }
                            )
                        } else {
                            BackHandler {
                                viewModel.onHideDetails()
                            }

                            RecipeDetailsScreen(
                                widthSizeClass = widthSizeClass,
                                recipeDetailsUiState = uiState.selectedRecipe,
                                navigateUp = navController::navigateUp
                            )
                        }
                    }

                }

                else -> {
                    Row {
                        Box(modifier = Modifier.weight(2f)) {
                            RecipesScreen(
                                uiState = uiState,
                                navigateUp = navController::navigateUp,
                                onRecipeSelected = { youtubeId ->
                                    viewModel.selectRecipe(
                                        youtubeId = youtubeId,
                                        showDetails = false
                                    )
                                }
                            )
                        }
                        Box(modifier = Modifier.weight(3f)) {
                            RecipeDetailsScreen(
                                widthSizeClass = widthSizeClass,
                                recipeDetailsUiState = uiState.selectedRecipe,
                                navigateUp = navController::navigateUp
                            )
                        }
                    }
                }
            }
        }
    }
}