package com.luczka.mycoffee.ui.screens.brewassistant.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.buildSpannedString
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import java.io.File

@Composable
fun BrewAssistantSelectionListItem(
    coffeeUiState: CoffeeUiState,
    isSelected: Boolean,
    onClick: (CoffeeUiState) -> Unit
) {
    val context = LocalContext.current

    val supportingText = buildSpannedString {
        val parts = listOfNotNull(
            coffeeUiState.roast?.let { stringResource(it.stringRes) },
            coffeeUiState.process?.let { stringResource(it.stringRes) }
        )
        append(parts.joinToString(separator = " • "))
    }.toString()

    ListItem(
        modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = { onClick(coffeeUiState) }, // TODO: Change how it works
                role = Role.Checkbox
            ),
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
        headlineContent = {
            Text(
                text = "${coffeeUiState.originOrName}, ${coffeeUiState.roasterOrBrand}",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(text = supportingText)
        },
        trailingContent = {
            Checkbox(
                checked = isSelected,
                onCheckedChange = null,
            )
        }
    )
}

@Preview
@Composable
private fun CoffeeSelectionListItemNotSelectedLightPreview() {
    CoffeeSelectionListItemPreview(darkTheme = false, isSelected = false)
}

@Preview
@Composable
private fun CoffeeSelectionListItemSelectedLightPreview() {
    CoffeeSelectionListItemPreview(darkTheme = false, isSelected = true)
}

@Preview
@Composable
private fun CoffeeSelectionListItemNotSelectedDarkPreview() {
    CoffeeSelectionListItemPreview(darkTheme = true, isSelected = false)
}

@Preview
@Composable
private fun CoffeeSelectionListItemSelectedDarkPreview() {
    CoffeeSelectionListItemPreview(darkTheme = true, isSelected = true)
}

@Composable
private fun CoffeeSelectionListItemPreview(darkTheme: Boolean, isSelected: Boolean) {
    val coffeeUiState = CoffeeUiState(
        originOrName = "Kolumbia",
        roasterOrBrand = "Mała Czarna",
    )
    MyCoffeeTheme(darkTheme = darkTheme) {
        BrewAssistantSelectionListItem(
            coffeeUiState = coffeeUiState,
            isSelected = isSelected,
            onClick = {},
        )
    }
}