package com.luczka.mycoffee.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.luczka.mycoffee.ui.models.MethodUiState
import com.luczka.mycoffee.ui.screens.coffees.CoffeesAction
import com.luczka.mycoffee.ui.screens.coffees.CoffeesScreen
import com.luczka.mycoffee.ui.screens.coffees.CoffeesViewModel
import com.luczka.mycoffee.ui.screens.equipments.EquipmentsAction
import com.luczka.mycoffee.ui.screens.equipments.EquipmentsScreen
import com.luczka.mycoffee.ui.screens.history.HistoryAction
import com.luczka.mycoffee.ui.screens.history.HistoryScreen
import com.luczka.mycoffee.ui.screens.history.HistoryViewModel
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
import kotlin.reflect.typeOf

@Composable
fun MyCoffeeNestedNavHost(
    widthSizeClass: WindowWidthSizeClass,
    modifier: Modifier,
    navController: NavHostController,
    onAction: (MainNavHostAction) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.Main.Home
    ) {
        composable<Routes.Main.Home> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is HomeAction.NavigateToAssistant -> onAction(MainNavHostAction.NavigateToAssistant)
                        is HomeAction.NavigateToBrewDetails -> onAction(MainNavHostAction.NavigateToBrewDetails(brewId = action.brewId))
                        is HomeAction.NavigateToCoffeeDetails -> onAction(MainNavHostAction.NavigateToCoffeeDetails(coffeeId = action.coffeeId))
                    }
                }
            )
        }
        composable<Routes.Main.History> {
            val viewModel = hiltViewModel<HistoryViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HistoryScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is HistoryAction.NavigateToAssistant -> onAction(MainNavHostAction.NavigateToAssistant)
                        is HistoryAction.NavigateToBrewDetails -> onAction(MainNavHostAction.NavigateToBrewDetails(brewId = action.brewId))
                    }
                }
            )
        }

        composable<Routes.Main.Coffees> {
            val viewModel = hiltViewModel<CoffeesViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CoffeesScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is CoffeesAction.NavigateUp -> navController.navigateUp()
                        is CoffeesAction.NavigateToAddCoffee -> onAction(MainNavHostAction.NavigateToCoffeeInput(coffeeId = null))
                        is CoffeesAction.NavigateToCoffeeDetails -> onAction(MainNavHostAction.NavigateToCoffeeDetails(coffeeId = action.coffeeId))
                        is CoffeesAction.OnEditClicked -> onAction(MainNavHostAction.NavigateToCoffeeInput(coffeeId = action.coffeeId))
                        else -> {}
                    }
                    viewModel.onAction(action)
                },
            )
        }

        composable<Routes.Main.Equipment> {
            EquipmentsScreen(
                onAction = { action ->
                    when(action) {
                        EquipmentsAction.NavigateToEquipmentInput -> onAction(MainNavHostAction.NavigateToEquipmentInput(equipmentId = null))
                    }
                }
            )
        }
        composable<Routes.Main.Methods> {
            val viewModel = hiltViewModel<MethodsViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MethodsScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is MethodsAction.NavigateToRecipes -> navController.navigate(Routes.Main.Recipes(action.methodUiState))
                    }
                }
            )
        }
        composable<Routes.Main.Recipes>(
            typeMap = mapOf(
                typeOf<MethodUiState>() to MyCoffeeNavTypes.MethodUiStateNavType
            )
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<Routes.Main.Recipes>()
            val viewModel = hiltViewModel<RecipesViewModel, RecipesViewModelFactory> { factory ->
                factory.create(arguments.methodUiState)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RecipesScreen(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is RecipesAction.NavigateUp -> navController.navigateUp()
                        is RecipesAction.NavigateToRecipeDetails -> navController.navigate(Routes.Main.RecipeDetails(action.recipeId))
                    }
                },
            )
        }
        composable<Routes.Main.RecipeDetails> { backStackEntry ->
            val arguments = backStackEntry.toRoute<Routes.Main.RecipeDetails>()
            val viewModel = hiltViewModel<RecipeDetailsViewModel, RecipeDetailsViewModelFactory> { factory ->
                factory.create(arguments.recipeId)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RecipeDetailsScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        RecipeDetailsAction.NavigateUp -> navController.navigateUp()
                    }
                }
            )
        }
    }
}