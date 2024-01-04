package com.luczka.mycoffee.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme

@Composable
fun HomeSectionRow(
    sectionTitle: String,
    sectionItems: List<CoffeeUiState>,
    navigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = sectionTitle,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(sectionItems) { coffeeCardUiState ->
                CoffeeCard(
                    onClick = {
                        // TODO: Handle click
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .wrapContentHeight(),
                    coffeeUiState = coffeeCardUiState,
                    imageAspectRatio = 1f / 1f,
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeSectionRowPreview() {
    val previewList = mutableListOf<CoffeeUiState>()
    (1..3).forEach { index ->
        previewList.add(
            CoffeeUiState(
                coffeeId = index,
                name = "Coffee",
                brand = "Brand"
            )
        )
    }
    MyCoffeeTheme {
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
    val previewList = mutableListOf<CoffeeUiState>()
    (1..5).forEach { index ->
        previewList.add(
            CoffeeUiState(
                coffeeId = index,
                name = "Coffee",
                brand = "Brand"
            )
        )
    }
    MyCoffeeTheme {
        HomeSectionRow(
            sectionTitle = "My coffee bags",
            sectionItems = previewList,
            navigate = {}
        )
    }
}