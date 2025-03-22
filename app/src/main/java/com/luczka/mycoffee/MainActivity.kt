package com.luczka.mycoffee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.luczka.mycoffee.ui.navigation.MyCoffeeMainNavHost
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Handle insets yourself https://www.youtube.com/watch?v=mlL6H-s0nF0
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MyCoffeeTheme {
                val mainNavController: NavHostController = rememberNavController()
                val navController: NavHostController = rememberNavController()

                val windowSize = calculateWindowSizeClass(this)

                MyCoffeeMainNavHost(
                    widthSizeClass =  windowSize.widthSizeClass,
                    mainNavController = mainNavController,
                    nestedNavController = navController
                )
            }
        }
    }
}