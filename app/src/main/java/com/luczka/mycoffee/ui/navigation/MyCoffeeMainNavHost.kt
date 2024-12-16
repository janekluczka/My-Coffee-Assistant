package com.luczka.mycoffee.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.luczka.mycoffee.ui.screens.assistant.AssistantAction
import com.luczka.mycoffee.ui.screens.assistant.AssistantViewModel
import com.luczka.mycoffee.ui.screens.assistant.screens.AssistantMainScreen
import com.luczka.mycoffee.ui.screens.assistantrating.AssistantRatingScreen
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsAction
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsScreen
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsViewModel
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsViewModelFactory
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputAction
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputScreen
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputViewModel
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputViewModelFactory
import com.luczka.mycoffee.ui.screens.equipmentinput.EquipmentInputAction
import com.luczka.mycoffee.ui.screens.equipmentinput.EquipmentInputScreen
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsAction
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsScreen
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsViewModel
import com.luczka.mycoffee.ui.screens.historydetails.HistoryDetailsViewModelFactory

sealed class MainNavHostAction {
    data object NavigateToAssistant : MainNavHostAction()
    data class NavigateToBrewDetails(val brewId: Long) : MainNavHostAction()
    data class NavigateToCoffeeDetails(val coffeeId: Long) : MainNavHostAction()
    data class NavigateToCoffeeInput(val coffeeId: Long?) : MainNavHostAction()
    data class NavigateToEquipmentInput(val equipmentId: Long?) : MainNavHostAction()
}

@Composable
fun MyCoffeeMainNavHost(
    widthSizeClass: WindowWidthSizeClass,
    mainNavController: NavHostController,
    nestedNavController: NavHostController
) {
    NavHost(
        navController = mainNavController,
        startDestination = Routes.Main,
    ) {
        composable<Routes.Main> {
            MainRoute(
                widthSizeClass = widthSizeClass,
                nestedNavController = nestedNavController,
                onAction = { action ->
                    when (action) {
                        is MainNavHostAction.NavigateToBrewDetails -> mainNavController.navigate(Routes.BrewDetails(action.brewId))
                        is MainNavHostAction.NavigateToAssistant -> mainNavController.navigate(Routes.Assistant)
                        is MainNavHostAction.NavigateToCoffeeDetails -> mainNavController.navigate(Routes.CoffeeDetails(action.coffeeId))
                        is MainNavHostAction.NavigateToCoffeeInput -> mainNavController.navigate(Routes.CoffeeInput(action.coffeeId))
                        is MainNavHostAction.NavigateToEquipmentInput -> mainNavController.navigate(Routes.EquipmentInput(action.equipmentId))
                    }
                }
            )
        }
        composable<Routes.BrewDetails>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideIntoContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideOutOfContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<Routes.BrewDetails>()
            val viewModel = hiltViewModel<HistoryDetailsViewModel, HistoryDetailsViewModelFactory> { factory ->
                factory.create(arguments.brewId)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HistoryDetailsScreen(
                historyDetailsUiState = uiState,
                onAction = { action ->
                    when (action) {
                        HistoryDetailsAction.NavigateUp -> mainNavController.navigateUp()
                        else -> {}
                    }
                    viewModel.onAction(action)
                }
            )
        }
        composable<Routes.Assistant>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideIntoContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideOutOfContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val viewModel = hiltViewModel<AssistantViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            AssistantMainScreen(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is AssistantAction.NavigateUp -> mainNavController.navigateUp()
                        is AssistantAction.NavigateToAssistantRating -> {
                            mainNavController.navigate(
                                route = Routes.AssistantRating(action.brewId),
                                navOptions = navOptions {
                                    popUpTo(Routes.Assistant) { inclusive = true }
                                }
                            )
                        }
                        else -> {}
                    }
                    viewModel.onAction(action)
                }
            )
        }
        composable<Routes.AssistantRating>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideIntoContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideOutOfContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<Routes.AssistantRating>()
            AssistantRatingScreen()
        }
        composable<Routes.CoffeeDetails>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideIntoContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideOutOfContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<Routes.CoffeeDetails>()
            val viewModel = hiltViewModel<CoffeeDetailsViewModel, CoffeeDetailsViewModelFactory> { factory ->
                factory.create(arguments.coffeeId)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CoffeeDetailsScreen(
                widthSizeClass = widthSizeClass,
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is CoffeeDetailsAction.NavigateUp -> mainNavController.navigateUp()
                        is CoffeeDetailsAction.OnEditClicked -> mainNavController.navigate(Routes.CoffeeInput(action.coffeeId))
                        else -> {}
                    }
                    viewModel.onAction(action)
                }
            )
        }
        composable<Routes.CoffeeInput>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideIntoContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideOutOfContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<Routes.CoffeeInput>()
            val viewModel = hiltViewModel<CoffeeInputViewModel, CoffeeInputViewModelFactory> { factory ->
                factory.create(arguments.coffeeId)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CoffeeInputScreen(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is CoffeeInputAction.NavigateUp -> mainNavController.navigateUp()
                        else -> {}
                    }
                    viewModel.onAction(action)
                }
            )
        }
        composable<Routes.EquipmentInput>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideIntoContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) + slideOutOfContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            EquipmentInputScreen(
                onAction = { action ->
                    when (action) {
                        is EquipmentInputAction.NavigateUp -> mainNavController.navigateUp()
                        else -> {}
                    }
                }
            )
        }
    }
}