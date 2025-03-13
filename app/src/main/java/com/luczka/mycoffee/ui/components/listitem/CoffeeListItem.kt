package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.buildSpannedString
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.FavoriteIcon
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.ProcessUiState
import com.luczka.mycoffee.ui.models.RoastUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import java.io.File

@Composable
fun CoffeesListItem(
    modifier: Modifier = Modifier,
    coffeeUiState: CoffeeUiState,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val supportingText = buildSpannedString {
        append(stringResource(R.string.format_coffee_amount_grams, coffeeUiState.amount))
        coffeeUiState.roast?.let { roastUiState ->
            append(" • ")
            append(stringResource(roastUiState.stringRes))
        }
        coffeeUiState.process?.let { processUiState ->
            append(" • ")
            append(stringResource(processUiState.stringRes))
        }
    }.toString()

    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        leadingContent = {
            Surface(
                modifier = Modifier.size(56.dp),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            ) {
                coffeeUiState.coffeeImages.firstOrNull()?.filename?.let { filename ->
                    val file = File(context.filesDir, filename)
                    val model = ImageRequest.Builder(context)
                        .data(file)
                        .build()
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = model,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        overlineContent = {
            Text(
                text = coffeeUiState.roasterOrBrand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        headlineContent = {
            Text(
                text = coffeeUiState.originOrName,
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
        },
        trailingContent = {
            if (coffeeUiState.isFavourite) {
                FavoriteIcon()
            }
        }
    )
}

@Preview
@Composable
fun CoffeeListItemLightPreview() {
    CoffeeListItemPreview(darkTheme = false)
}

@Preview
@Composable
fun CoffeeListItemDarkPreview() {
    CoffeeListItemPreview(darkTheme = true)
}

@Composable
private fun CoffeeListItemPreview(darkTheme: Boolean) {
    val coffeeUiState = CoffeeUiState(
        coffeeId = 1,
        originOrName = "salwador finca",
        roasterOrBrand = "monko.",
        amount = "200.0",
        roast = RoastUiState.Medium,
        process = ProcessUiState.Other
    )
    MyCoffeeTheme(darkTheme = darkTheme) {
        CoffeesListItem(
            coffeeUiState = coffeeUiState,
            onClick = {}
        )
    }
}