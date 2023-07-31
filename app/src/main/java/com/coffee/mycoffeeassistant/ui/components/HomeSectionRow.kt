package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coffee.mycoffeeassistant.ui.model.components.CoffeeCardUiState
import com.coffee.mycoffeeassistant.ui.navigation.Screen
import com.coffee.mycoffeeassistant.ui.theme.MyCoffeeAssistantTheme

@Composable
fun HomeSectionRow(
    sectionTitle: String,
    sectionItems: List<CoffeeCardUiState>,
    navigate: (String) -> Unit
) {
    val displayedItems = sectionItems.take(4)
    val showButton = sectionItems.size > 4
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = sectionTitle,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            style = MaterialTheme.typography.headlineSmall
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(displayedItems) { coffeeCardUiState ->
                VerticalCoffeeCard(
                    onClick = {
                        navigate(Screen.HomeCoffeeDetails.createRoute(id = coffeeCardUiState.id))
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .wrapContentHeight(),
                    coffeeCardUiState = coffeeCardUiState,
                    imageAspectRatio = 4f / 3f,
                )
            }
            if (showButton) {
                item {
                    FilledTonalIconButton(
                        onClick = { /* TODO: Add more screen */ },
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp),
                        content = {
                            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeSectionRowPreview() {
    val previewList = mutableListOf<CoffeeCardUiState>()
    (1..3).forEach { index ->
        previewList.add(
            CoffeeCardUiState(
                id = index,
                name = "Coffee $index",
                brand = "Brand $index"
            )
        )
    }
    MyCoffeeAssistantTheme {
        HomeSectionRow(
            sectionTitle = "My coffee bags",
            sectionItems = previewList,
            navigate = {}
        )
    }
}

@Preview
@Composable
fun HomeSectionRowWithButtonPreview() {
    val previewList = mutableListOf<CoffeeCardUiState>()
    (1..5).forEach { index ->
        previewList.add(
            CoffeeCardUiState(
                id = index,
                name = "Coffee $index",
                brand = "Brand $index"
            )
        )
    }
    MyCoffeeAssistantTheme {
        HomeSectionRow(
            sectionTitle = "My coffee bags",
            sectionItems = previewList,
            navigate = {}
        )
    }
}