package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.coffee.mycoffeeassistant.ui.navigation.Screen


@Composable
fun HomeSection(navController: NavController, sectionTitle: String) {
    HomeSectionHeader(text = sectionTitle)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        items(3) { index ->
            CoffeeCardVertical(
                modifier = Modifier
                    .width(150.dp)
                    .wrapContentHeight(),
                index = index) {
                navController.navigate(Screen.CoffeeDetails.route + "/$index")
            }
        }
    }
}

@Preview
@Composable
fun HomeSectionPreview() {
    HomeSection(navController = rememberNavController(), sectionTitle = "My coffee bags")
}



