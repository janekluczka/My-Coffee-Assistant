package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.ui.models.BrewingStepUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme

@Composable
fun BrewingStepListItem(brewingStepUiState: BrewingStepUiState) {
    ListItem(
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${brewingStepUiState.number}")
            }
        },
        headlineContent = {
            Text(text = brewingStepUiState.description)
        },
        trailingContent = {
            brewingStepUiState.time?.let { Text(text = it) }
        }
    )
}

@Preview
@Composable
private fun LightThemeBrewingStepListItemPreview() {
    BrewingStepListItemPreview(darkTheme = false)
}

@Preview
@Composable
private fun LightThemeBrewingStepListItemWithoutTrailingContentPreview() {
    BrewingStepListItemWithoutTrailingContentPreview(darkTheme = false)
}

@Preview
@Composable
private fun DarkThemeBrewingStepListItemPreview() {
    BrewingStepListItemPreview(darkTheme = true)
}

@Preview
@Composable
private fun DarkThemeBrewingStepListItemWithoutTrailingContentPreview() {
    BrewingStepListItemWithoutTrailingContentPreview(darkTheme = true)
}

@Composable
private fun BrewingStepListItemPreview(darkTheme: Boolean) {
    val brewingStepUiState = BrewingStepUiState(
        number = 1,
        description = "Grind coffee beans (15 grams) relatively fine",
        time = "30-150 s"
    )
    MyCoffeeTheme(darkTheme = darkTheme) {
        BrewingStepListItem(brewingStepUiState = brewingStepUiState)
    }
}

@Composable
private fun BrewingStepListItemWithoutTrailingContentPreview(darkTheme: Boolean) {
    val brewingStepUiState = BrewingStepUiState(
        number = 1,
        description = "Grind coffee beans (15 grams) relatively fine",
        time = null,
    )
    MyCoffeeTheme(darkTheme = darkTheme) {
        BrewingStepListItem(brewingStepUiState = brewingStepUiState)
    }
}