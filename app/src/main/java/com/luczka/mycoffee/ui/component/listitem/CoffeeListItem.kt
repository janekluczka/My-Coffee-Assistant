package com.luczka.mycoffee.ui.component.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeListItem(
    coffeeUiState: CoffeeUiState,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    ListItem(
        modifier = Modifier.clickable { onClick() },
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
                text = coffeeUiState.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingText = {
            Text(
                text = coffeeUiState.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            if (coffeeUiState.isFavourite) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null
                )
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
        name = "salwador finca",
        brand = "monko."
    )
    MyCoffeeTheme(darkTheme = darkTheme) {
        CoffeeListItem(
            coffeeUiState = coffeeUiState,
            onClick = {}
        )
    }
}