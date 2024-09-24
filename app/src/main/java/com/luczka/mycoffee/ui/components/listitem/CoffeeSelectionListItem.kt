package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeSelectionListItem(
    coffeeUiState: CoffeeUiState,
    isSelected: Boolean,
    onClick: (CoffeeUiState) -> Unit
) {
    val context = LocalContext.current

    ListItem(
        modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = { onClick(coffeeUiState) }, // TODO: Change how it works
                role = Role.Checkbox
            ),
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                contentAlignment = Alignment.Center
            ) {
                coffeeUiState.imageFile240x240?.let { imageFile ->
                    val cacheFile = File(context.filesDir, imageFile)
                    val model = ImageRequest.Builder(context)
                        .data(cacheFile)
                        .build()
                    AsyncImage(model = model, contentDescription = null)
                }
            }
        },
        headlineText = {
            Text(
                text = "${coffeeUiState.name}, ${coffeeUiState.brand}",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingText = {
            coffeeUiState.amount?.let { coffeeAmount ->
                Text(
                    text = stringResource(
                        id = R.string.coffee_parameters_amount_with_unit,
                        coffeeAmount
                    )
                )
            }
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
fun CoffeeSelectionListItemNotSelectedLightPreview() {
    CoffeeSelectionListItemPreview(darkTheme = false, isSelected = false)
}

@Preview
@Composable
fun CoffeeSelectionListItemSelectedLightPreview() {
    CoffeeSelectionListItemPreview(darkTheme = false, isSelected = true)
}

@Preview
@Composable
fun CoffeeSelectionListItemNotSelectedDarkPreview() {
    CoffeeSelectionListItemPreview(darkTheme = true, isSelected = false)
}

@Preview
@Composable
fun CoffeeSelectionListItemSelectedDarkPreview() {
    CoffeeSelectionListItemPreview(darkTheme = true, isSelected = true)
}

@Composable
private fun CoffeeSelectionListItemPreview(darkTheme: Boolean, isSelected: Boolean) {
    val coffeeUiState = CoffeeUiState(
        name = "Kolumbia",
        brand = "Mała Czarna",
        amount = "250"
    )
    MyCoffeeTheme(darkTheme = darkTheme) {
        CoffeeSelectionListItem(
            coffeeUiState = coffeeUiState,
            isSelected = isSelected,
            onClick = {},
        )
    }
}