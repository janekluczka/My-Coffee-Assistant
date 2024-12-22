package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.buildSpannedString
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.BrewUiState

@Composable
fun BrewListItem(
    brewUiState: BrewUiState,
    onClick: () -> Unit
) {
    val headlineText = if (brewUiState.brewedCoffees.isEmpty()) {
        stringResource(R.string.none)
    } else {
        brewUiState.brewedCoffees.joinToString(separator = " + ") { it.coffee.originOrName }
    }

    val supportingText = buildSpannedString {
        append("${brewUiState.coffeeAmount} g of coffee")
        append(" • ")
        append("${brewUiState.coffeeRatio}:${brewUiState.waterRatio} ratio")
    }.toString()

    ListItem(
        modifier = Modifier.clickable {
            onClick()
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (brewUiState.rating != null) {
                    Text(text = "${brewUiState.rating}")
                }
            }
        },
        overlineContent = {
            Text(
                text = brewUiState.formattedDate,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium
            )
        },
        headlineContent = {
            Text(
                text = headlineText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = supportingText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}