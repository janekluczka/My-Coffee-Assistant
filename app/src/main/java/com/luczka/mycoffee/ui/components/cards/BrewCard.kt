package com.luczka.mycoffee.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.core.text.buildSpannedString
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.BrewUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BrewCard(
    modifier: Modifier,
    brewUiState: BrewUiState,
    onClick: () -> Unit
) {
    val density = LocalDensity.current

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

    val overlineTextStyle = MaterialTheme.typography.labelMedium
    val headlineTextStyle = MaterialTheme.typography.titleMedium
    val supportingTextStyle = MaterialTheme.typography.bodyMedium

    val overlineTextLineHeight = with(density) { overlineTextStyle.lineHeight.toDp() }
    val headlineTextLineHeight = with(density) { headlineTextStyle.lineHeight.toDp() }
    val supportingTextLineHeight = with(density) { supportingTextStyle.lineHeight.toDp() }

    val columnPadding = 16.dp
    val columnHeight = overlineTextLineHeight + headlineTextLineHeight + supportingTextLineHeight

    val cardHeight = columnHeight + 2 * columnPadding

    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row {
            Box(
                modifier = Modifier
                    .size(cardHeight)
                    .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (brewUiState.rating != null) {
                    Text(text = "${brewUiState.rating}")
                }
            }
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(columnPadding)
            ) {
                Text(
                    text = brewUiState.formattedDate,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = headlineText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = supportingText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}