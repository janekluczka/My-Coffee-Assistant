package com.luczka.mycoffee.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
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
import com.luczka.mycoffee.ui.models.RecipeUiState
import com.luczka.mycoffee.ui.screens.brews.BrewsAction
import com.luczka.mycoffee.ui.screens.brews.BrewsScreen
import com.luczka.mycoffee.ui.screens.brews.BrewsViewModel
import com.luczka.mycoffee.ui.screens.coffees.CoffeesAction
import com.luczka.mycoffee.ui.screens.coffees.CoffeesScreen
import com.luczka.mycoffee.ui.screens.coffees.CoffeesViewModel
import com.luczka.mycoffee.ui.screens.equipments.EquipmentsAction
import com.luczka.mycoffee.ui.screens.equipments.EquipmentsScreen
import com.luczka.mycoffee.ui.screens.home.HomeScreen
import com.luczka.mycoffee.ui.screens.home.HomeViewModel
import com.luczka.mycoffee.ui.screens.recipecategories.RecipeCategoriesAction
import com.luczka.mycoffee.ui.screens.recipecategories.RecipeCategoriesScreen
import com.luczka.mycoffee.ui.screens.recipecategories.RecipeCategoriesViewModel
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
    onAction: (MainAction) -> Unit,
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = NestedNavHostRoutes.Home
    ) {
        composable<NestedNavHostRoutes.Home> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = viewModel::onAction
            )
        }
        composable<NestedNavHostRoutes.Brews> {
            val viewModel = hiltViewModel<BrewsViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            BrewsScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is BrewsAction.NavigateToAssistant -> onAction(MainAction.NavigateToAssistant)
                        is BrewsAction.NavigateToBrewDetails -> onAction(MainAction.NavigateToBrewDetails(brewId = action.brewId))
                        else -> {}
                    }
                    viewModel.onAction(action)
                }
            )
        }

        composable<NestedNavHostRoutes.Coffees> {
            val viewModel = hiltViewModel<CoffeesViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CoffeesScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is CoffeesAction.NavigateUp -> navController.navigateUp()
                        is CoffeesAction.NavigateToAddCoffee -> onAction(MainAction.NavigateToCoffeeInput(coffeeId = null))
                        is CoffeesAction.NavigateToCoffeeDetails -> onAction(MainAction.NavigateToCoffeeDetails(coffeeId = action.coffeeId))
                        is CoffeesAction.OnEditClicked -> onAction(MainAction.NavigateToCoffeeInput(coffeeId = action.coffeeId))
                        else -> {}
                    }
                    viewModel.onAction(action)
                },
            )
        }

        composable<NestedNavHostRoutes.Equipment> {
            EquipmentsScreen(
                onAction = { action ->
                    when(action) {
                        EquipmentsAction.NavigateToEquipmentInput -> onAction(MainAction.NavigateToEquipmentInput(equipmentId = null))
                    }
                }
            )
        }
        composable<NestedNavHostRoutes.RecipeCategories> {
            val viewModel = hiltViewModel<RecipeCategoriesViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RecipeCategoriesScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is RecipeCategoriesAction.NavigateToRecipes -> navController.navigate(NestedNavHostRoutes.Recipes(action.methodUiState))
                    }
                }
            )
        }
        composable<NestedNavHostRoutes.Recipes>(
            typeMap = mapOf(
                typeOf<MethodUiState>() to CustomNavTypes.MethodUiStateNavType
            )
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<NestedNavHostRoutes.Recipes>()
            val viewModel = hiltViewModel<RecipesViewModel, RecipesViewModelFactory> { factory ->
                factory.create(arguments.methodUiState)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RecipesScreen(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is RecipesAction.NavigateUp -> navController.navigateUp()
                        is RecipesAction.NavigateToRecipeDetails -> navController.navigate(NestedNavHostRoutes.RecipeDetails(action.recipeUiState))
                    }
                },
            )
        }
        composable<NestedNavHostRoutes.RecipeDetails>(
            typeMap = mapOf(
                typeOf<RecipeUiState>() to CustomNavTypes.RecipeUiStateNavType
            )
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<NestedNavHostRoutes.RecipeDetails>()
            val viewModel = hiltViewModel<RecipeDetailsViewModel, RecipeDetailsViewModelFactory> { factory ->
                factory.create(arguments.recipeUiState)
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