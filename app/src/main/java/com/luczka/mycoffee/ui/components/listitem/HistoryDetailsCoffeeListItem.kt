package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.CoffeeUiState
import java.io.File

@Composable
fun HistoryDetailsCoffeeListItem(coffeeUiState: CoffeeUiState?) {
    val context = LocalContext.current

    val headlineText = if (coffeeUiState == null) {
        stringResource(R.string.unknown)
    } else {
        stringResource(
            id = R.string.coffee_parameters_name_and_brand,
            coffeeUiState.name,
            coffeeUiState.brand
        )
    }

    ListItem(
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                contentAlignment = Alignment.Center
            ) {
                coffeeUiState?.imageFile240x240?.let { imageFile ->
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
        },
        headlineContent = {
            Text(
                text = headlineText,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}