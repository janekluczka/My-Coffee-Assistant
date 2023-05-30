package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.navigation.Screen


@Composable
fun HomeSection(
    navController: NavController,
    list: List<CoffeeUiState>,
    sectionTitle: String
) {
    HomeSectionHeader(text = sectionTitle)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        items(list) { coffeeUiState ->
            VerticalCoffeeCard(
                coffeeUiState = coffeeUiState,
                imageAspectRatio = 1f / 1f,
                modifier = Modifier
                    .width(120.dp)
                    .wrapContentHeight()
            ) {
                navController.navigate(Screen.CoffeeDetails.createRoute(id = coffeeUiState.id))
            }
        }
    }
}

@Preview
@Composable
fun HomeSectionPreview() {
    HomeSection(
        navController = rememberNavController(),
        list = emptyList(),
        sectionTitle = "My coffee bags"
    )
}



