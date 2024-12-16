package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
            id = R.string.format_coffee_name_coma_brand,
            coffeeUiState.originOrName,
            coffeeUiState.roasterOrBrand
        )
    }

    ListItem(
        leadingContent = {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            ) {
                coffeeUiState?.coffeeImages?.firstOrNull()?.filename?.let { filename ->
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
                text = headlineText,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}