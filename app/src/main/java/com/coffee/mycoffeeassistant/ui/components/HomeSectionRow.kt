package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun HomeSectionRow(
    navController: NavController,
    list: List<CoffeeUiState>,
    sectionTitle: String
) {
    Column {
        HomeSectionHeader(text = sectionTitle)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            items(list) { coffeeUiState ->
                VerticalCoffeeCard(
                    name = coffeeUiState.name,
                    brand = coffeeUiState.brand,
                    bitmap = coffeeUiState.bitmap,
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
}

@Preview
@Composable
fun HomeSectionRowPreview() {
    val previewList = listOf(
        CoffeeUiState(name = "Coffee name", brand = "Brand"),
        CoffeeUiState(name = "Coffee", brand = "Known Brand"),
        CoffeeUiState(name = "Coffee", brand = "Brand")
    )
    HomeSectionRow(
        navController = rememberNavController(),
        list = previewList,
        sectionTitle = "My coffee bags"
    )
}



