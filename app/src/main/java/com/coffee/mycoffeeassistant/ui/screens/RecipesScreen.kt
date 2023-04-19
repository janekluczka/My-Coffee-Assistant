@file:OptIn(ExperimentalMaterial3Api::class)

package com.coffee.mycoffeeassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@Composable
fun RecipesScreen(navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        items(3) { index ->
            Card(
                onClick = { navController.navigate(Screen.MethodRecipes.route) },
                modifier = Modifier
                    .height(height = 150.dp)
                    .fillMaxWidth()
            ) {
                Box(Modifier.fillMaxSize()) {
                    Text("Recipe $index", Modifier.align(Alignment.Center))
                }
            }
        }
    }
}