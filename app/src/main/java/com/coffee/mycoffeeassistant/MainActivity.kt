package com.coffee.mycoffeeassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.coffee.mycoffeeassistant.ui.MyCoffeeAssistantApp
import com.coffee.mycoffeeassistant.ui.theme.MyCoffeeAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCoffeeAssistantTheme {
                MyCoffeeAssistantApp()
            }
        }
    }
}