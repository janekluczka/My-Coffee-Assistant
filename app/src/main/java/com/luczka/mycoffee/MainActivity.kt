package com.luczka.mycoffee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.luczka.mycoffee.ui.MyCoffeeApp
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Handle insets yourself https://www.youtube.com/watch?v=mlL6H-s0nF0
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MyCoffeeTheme {
                val windowSize = calculateWindowSizeClass(this)
                MyCoffeeApp(widthSizeClass = windowSize.widthSizeClass)
            }
        }
    }
}