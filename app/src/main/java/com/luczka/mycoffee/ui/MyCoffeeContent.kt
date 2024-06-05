package com.luczka.mycoffee.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.luczka.mycoffee.ui.navigation.MyCoffeeMainNavHost

@Composable
fun MyCoffeeContent(widthSizeClass: WindowWidthSizeClass) {
    val mainNavController: NavHostController = rememberNavController()
    val navController: NavHostController = rememberNavController()

    MyCoffeeMainNavHost(
        widthSizeClass = widthSizeClass,
        mainNavController = mainNavController,
        nestedNavController = navController
    )
}