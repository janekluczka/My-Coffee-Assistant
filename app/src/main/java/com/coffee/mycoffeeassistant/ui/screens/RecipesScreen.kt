@file:OptIn(ExperimentalMaterial3Api::class)

package com.coffee.mycoffeeassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.coffee.mycoffeeassistant.ui.components.MethodCard
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@Composable
fun RecipesScreen(navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        items(3) { index ->
            MethodCard {
                navController.navigate(Screen.MethodRecipes.route)
            }
        }
    }
}