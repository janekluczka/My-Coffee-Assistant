package com.luczka.mycoffee.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.luczka.mycoffee.ui.screens.brews.BrewsNavigationEvent
import com.luczka.mycoffee.ui.screens.brews.BrewsScreen
import com.luczka.mycoffee.ui.screens.brews.BrewsViewModel
import com.luczka.mycoffee.ui.screens.coffees.CoffeesNavigationEvent
import com.luczka.mycoffee.ui.screens.coffees.CoffeesScreen
import com.luczka.mycoffee.ui.screens.coffees.CoffeesViewModel
import com.luczka.mycoffee.ui.screens.equipments.EquipmentsAction
import com.luczka.mycoffee.ui.screens.equipments.EquipmentsScreen
import com.luczka.mycoffee.ui.screens.home.HomeScreen
import com.luczka.mycoffee.ui.screens.home.HomeViewModel
import com.luczka.mycoffee.ui.screens.recipecategories.RecipeCategoriesNavigationEvent
import com.luczka.mycoffee.ui.screens.recipecategories.RecipeCategoriesScreen
import com.luczka.mycoffee.ui.screens.recipecategories.RecipeCategoriesViewModel
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsNavigationEvent
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsScreen
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsViewModel
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsViewModelFactory
import com.luczka.mycoffee.ui.screens.recipes.RecipesNavigationEvent
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

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        BrewsNavigationEvent.NavigateToAssistant -> onAction(MainAction.NavigateToAssistant)
                        is BrewsNavigationEvent.NavigateToBrewDetails -> onAction(MainAction.NavigateToBrewDetails(event.brewId))
                    }
                }
            }

            BrewsScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = viewModel::onAction
            )
        }

        composable<NestedNavHostRoutes.Coffees> {
            val viewModel = hiltViewModel<CoffeesViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        CoffeesNavigationEvent.NavigateUp -> navController.navigateUp()
                        CoffeesNavigationEvent.NavigateToAddCoffee -> onAction(MainAction.NavigateToCoffeeInput(coffeeId = null))
                        is CoffeesNavigationEvent.NavigateToCoffeeDetails -> onAction(MainAction.NavigateToCoffeeDetails(coffeeId = event.coffeeId))
                        is CoffeesNavigationEvent.NavigateToEditCoffee -> onAction(MainAction.NavigateToCoffeeInput(coffeeId = event.coffeeId))
                    }
                }
            }

            CoffeesScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = viewModel::onAction
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

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        is RecipeCategoriesNavigationEvent.NavigateToMethodDetails -> navController.navigate(NestedNavHostRoutes.Recipes(event.methodUiState))
                    }
                }
            }

            RecipeCategoriesScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = viewModel::onAction
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

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        RecipesNavigationEvent.NavigateUp -> navController.navigateUp()
                        is RecipesNavigationEvent.NavigateToRecipeDetails -> navController.navigate(NestedNavHostRoutes.RecipeDetails(event.recipeUiState))
                    }
                }
            }

            RecipesScreen(
                uiState = uiState,
                onAction = viewModel::onAction
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

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        RecipeDetailsNavigationEvent.NavigateUp -> navController.navigateUp()
                    }
                }
            }

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RecipeDetailsScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = viewModel::onAction
            )
        }
    }
}