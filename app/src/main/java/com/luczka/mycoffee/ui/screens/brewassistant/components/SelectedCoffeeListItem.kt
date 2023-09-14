package com.luczka.mycoffee.ui.screens.brewassistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.model.CoffeeUiState
import java.io.File

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SelectedCoffeeListItem(
    index: Int,
    showCoffeeIndex: Boolean = false,
    coffeeUiState: CoffeeUiState,
    selectedAmount: String? = null
) {
    if (showCoffeeIndex) {
        ListItem(
            leadingContent = { Number(index) },
            overlineText = {
                Text(
                    text = stringResource(
                        id = R.string.assistant_coffee_index,
                        index + 1
                    )
                )
            },
            headlineText = { HeadlineText(coffeeUiState) },
            supportingText = { SupportingText(coffeeUiState, selectedAmount) },
            trailingContent = { Image(coffeeUiState) }
        )
    } else {
        ListItem(
            leadingContent = { Number(index) },
            headlineText = { HeadlineText(coffeeUiState) },
            supportingText = { SupportingText(coffeeUiState, selectedAmount) },
            trailingContent = { Image(coffeeUiState) }
        )
    }
}

@Composable
private fun Number(index: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
        contentAlignment = Alignment.Center,
        content = { Text(text = "${index + 1}") }
    )
}

@Composable
private fun HeadlineText(coffeeUiState: CoffeeUiState) {
    Text(
        text = stringResource(
            id = R.string.coffee_parameters_name_and_brand,
            coffeeUiState.name,
            coffeeUiState.brand
        ),
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun SupportingText(
    coffeeUiState: CoffeeUiState,
    selectedAmount: String?
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(
                id = R.string.coffee_parameters_amount_with_unit,
                coffeeUiState.amount.toString()
            )
        )
        selectedAmount?.let { amount ->
            Text(
                text = stringResource(
                    id = R.string.assistant_taken_amount,
                    amount
                ),
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun Image(coffeeUiState: CoffeeUiState) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
        contentAlignment = Alignment.Center
    ) {
        coffeeUiState.imageFile240x240?.let { imageFile ->
            val cacheFile = File(context.filesDir, imageFile)
            val model = ImageRequest.Builder(context)
                .data(cacheFile)
                .build()
            AsyncImage(
                model = model,
                contentDescription = null
            )
        }
    }
}