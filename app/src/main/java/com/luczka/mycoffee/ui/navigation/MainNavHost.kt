package com.luczka.mycoffee.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantOneTimeEvent
import com.luczka.mycoffee.ui.screens.brewassistant.BrewAssistantViewModel
import com.luczka.mycoffee.ui.screens.brewassistant.screen.BrewAssistantMainScreen
import com.luczka.mycoffee.ui.screens.brewdetails.BrewDetailsOneTimeEvent
import com.luczka.mycoffee.ui.screens.brewdetails.BrewDetailsScreen
import com.luczka.mycoffee.ui.screens.brewdetails.BrewDetailsViewModel
import com.luczka.mycoffee.ui.screens.brewdetails.BrewDetailsViewModelFactory
import com.luczka.mycoffee.ui.screens.brewrating.AssistantRatingScreen
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsOneTimeEvent
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsScreen
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsViewModel
import com.luczka.mycoffee.ui.screens.coffeedetails.CoffeeDetailsViewModelFactory
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputOneTimeEvent
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputScreen
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputViewModel
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputViewModelFactory
import com.luczka.mycoffee.ui.screens.equipmentinput.EquipmentInputAction
import com.luczka.mycoffee.ui.screens.equipmentinput.EquipmentInputScreen
import kotlinx.coroutines.launch

private val drawerRoutes = listOf(
    TopLevelRoute(
        route = NestedNavHostRoutes.Home,
        stringRes = R.string.tab_home,
        drawableRes = R.drawable.ic_home_24_fill_0_weight_300_grade_0_opticalsize_24
    ),
    TopLevelRoute(
        route = NestedNavHostRoutes.Brews,
        stringRes = R.string.tab_history,
        drawableRes = R.drawable.ic_history_24_fill_0_weight_300_grade_0_opticalsize_24
    ),
    TopLevelRoute(
        route = NestedNavHostRoutes.Coffees,
        stringRes = R.string.tab_coffees,
        drawableRes = R.drawable.ic_browse_24_fill_0_weight_300_grade_0_opticalsize_24
    ),
//            TopLevelRoute(
//                route = Equipment,
//                stringRes = R.string.tab_equipment,
//                drawableRes = R.drawable.ic_coffee_maker_24_fill_0_weight_300_grade_0_opticalsize_24
//            ),
    TopLevelRoute(
        route = NestedNavHostRoutes.Recipes.Categories,
        stringRes = R.string.tab_recipes,
        drawableRes = R.drawable.ic_list_alt_24_fill_0_weight_300_grade_0_opticalsize_24
    )
)

@Composable
fun MyCoffeeMainNavHost(
    widthSizeClass: WindowWidthSizeClass,
    mainNavController: NavHostController,
    nestedNavController: NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val mainNavBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val nestedNavBackStackEntry by nestedNavController.currentBackStackEntryAsState()

    val currentMainDestination = mainNavBackStackEntry?.destination
    val currentNestedDestination = nestedNavBackStackEntry?.destination

    val isMainRouteCurrentMainDestination = currentMainDestination?.hasRoute(MainNavHostRoute.Main::class) ?: false

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isMainRouteCurrentMainDestination,
        drawerContent = {
            ModalDrawerSheet(drawerTonalElevation = 0.dp) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(state = rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    drawerRoutes.forEach { topLevelRoute ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = topLevelRoute.drawableRes),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(text = stringResource(id = topLevelRoute.stringRes))
                            },
                            selected = isMainRouteCurrentMainDestination && currentNestedDestination?.hierarchy?.any {
                                it.hasRoute(topLevelRoute.route::class)
                            } == true,
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                                nestedNavController.navigate(topLevelRoute.route) {
                                    popUpTo(nestedNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    ) {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = mainNavController,
            startDestination = MainNavHostRoute.Main,
        ) {
            composable<MainNavHostRoute.Main> {
                MainRoute(
                    widthSizeClass = widthSizeClass,
                    nestedNavController = nestedNavController,
                    onMenuButtonClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    },
                    navigateToAssistant = mainNavController::navigateToBrewAssistant,
                    navigateToBrewDetails = mainNavController::navigateToBrewDetails,
                    navigateToCoffeeDetails = mainNavController::navigateToCoffeeDetails,
                    navigateToCoffeeInput = mainNavController::navigateToCoffeeInput,
                    navigateToEquipmentInput = mainNavController::navigateToEquipmentInput
                )
            }
            composable<MainNavHostRoute.BrewDetails>(
                enterTransition = fadeInAndSlideToStart(),
                exitTransition = fadeOutAndSlideToEnd()
            ) { backStackEntry ->
                val arguments = backStackEntry.toRoute<MainNavHostRoute.BrewDetails>()
                val viewModel = hiltViewModel<BrewDetailsViewModel, BrewDetailsViewModelFactory> { factory ->
                    factory.create(arguments.brewId)
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.oneTimeEvent.collect { event ->
                        when (event) {
                            BrewDetailsOneTimeEvent.NavigateUp -> mainNavController.navigateUp()
                        }
                    }
                }

                BrewDetailsScreen(
                    brewDetailsUiState = uiState,
                    onAction = viewModel::onAction
                )
            }
            composable<MainNavHostRoute.BrewAssistant>(
                enterTransition = fadeInAndSlideToStart(),
                exitTransition = fadeOutAndSlideToEnd()
            ) {
                val viewModel = hiltViewModel<BrewAssistantViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.oneTimeEvent.collect { event ->
                        when (event) {
                            BrewAssistantOneTimeEvent.NavigateUp -> mainNavController.navigateUp()
                            is BrewAssistantOneTimeEvent.NavigateToBrewRating -> mainNavController.navigateToBrewRating(event.brewId)
                        }
                    }
                }

                BrewAssistantMainScreen(
                    uiState = uiState,
                    onAction = viewModel::onAction
                )
            }
            composable<MainNavHostRoute.BrewRating>(
                enterTransition = fadeInAndSlideToStart(),
                exitTransition = fadeOutAndSlideToEnd()
            ) { backStackEntry ->
                val arguments = backStackEntry.toRoute<MainNavHostRoute.BrewRating>()
                AssistantRatingScreen()
            }
            composable<MainNavHostRoute.CoffeeDetails>(
                enterTransition = fadeInAndSlideToStart(),
                exitTransition = fadeOutAndSlideToEnd()
            ) { backStackEntry ->
                val arguments = backStackEntry.toRoute<MainNavHostRoute.CoffeeDetails>()
                val viewModel = hiltViewModel<CoffeeDetailsViewModel, CoffeeDetailsViewModelFactory> { factory ->
                    factory.create(arguments.coffeeId)
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.oneTimeEvent.collect { event ->
                        when (event) {
                            CoffeeDetailsOneTimeEvent.NavigateUp -> mainNavController.navigateUp()
                            is CoffeeDetailsOneTimeEvent.NavigateToCoffeeInput -> mainNavController.navigateToCoffeeInput(event.coffeeId)
                        }
                    }
                }

                CoffeeDetailsScreen(
                    widthSizeClass = widthSizeClass,
                    uiState = uiState,
                    onAction = viewModel::onAction
                )
            }
            composable<MainNavHostRoute.CoffeeInput>(
                enterTransition = fadeInAndSlideToStart(),
                exitTransition = fadeOutAndSlideToEnd()
            ) { backStackEntry ->
                val arguments = backStackEntry.toRoute<MainNavHostRoute.CoffeeInput>()
                val viewModel = hiltViewModel<CoffeeInputViewModel, CoffeeInputViewModelFactory> { factory ->
                    factory.create(arguments.coffeeId)
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.oneTimeEvent.collect { event ->
                        when (event) {
                            CoffeeInputOneTimeEvent.NavigateUp -> mainNavController.navigateUp()
                        }
                    }
                }

                CoffeeInputScreen(
                    uiState = uiState,
                    onAction = viewModel::onAction
                )
            }
            composable<MainNavHostRoute.EquipmentInput>(
                enterTransition = fadeInAndSlideToStart(),
                exitTransition = fadeOutAndSlideToEnd()
            ) {
                EquipmentInputScreen(
                    onAction = { action ->
                        when (action) {
                            EquipmentInputAction.NavigateUp -> mainNavController.navigateUp()
                        }
                    }
                )
            }
        }
    }
}

private fun fadeInAndSlideToStart(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition? = {
    fadeIn(
        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    ) + slideIntoContainer(
        animationSpec = tween(durationMillis = 300, easing = EaseIn),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    )
}

private fun fadeOutAndSlideToEnd(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition? = {
    fadeOut(
        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    ) + slideOutOfContainer(
        animationSpec = tween(durationMillis = 300, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.End
    )
}

private fun NavHostController.navigateToBrewAssistant() {
    navigate(MainNavHostRoute.BrewAssistant)
}

private fun NavHostController.navigateToBrewRating(brewId: Long) {
    navigate(MainNavHostRoute.BrewRating(brewId)) {
        popUpTo(MainNavHostRoute.BrewAssistant) { inclusive = true }
    }
}

private fun NavHostController.navigateToBrewDetails(brewId: Long) {
    navigate(MainNavHostRoute.BrewDetails(brewId))
}

private fun NavHostController.navigateToCoffeeDetails(coffeeId: Long) {
    navigate(MainNavHostRoute.CoffeeDetails(coffeeId))
}

private fun NavHostController.navigateToCoffeeInput(coffeeId: Long?) {
    navigate(MainNavHostRoute.CoffeeInput(coffeeId))
}

private fun NavHostController.navigateToEquipmentInput(equipmentId: Long?) {

}