package com.luczka.mycoffee.ui.screens.brewassistant.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.CoffeeUiState
import java.io.File

@Composable
fun AssistantSummaryCoffeeListItem(
    coffeeUiState: CoffeeUiState,
    selectedAmount: String? = null
) {
    val context = LocalContext.current
    ListItem(
        leadingContent = {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(4.dp),
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
                text = stringResource(
                    id = R.string.format_coffee_name_coma_brand,
                    coffeeUiState.originOrName,
                    coffeeUiState.roasterOrBrand
                ),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(text = buildCoffeeAmountTakenString(coffeeUiState, selectedAmount))
        }
    )
}

@Composable
private fun buildCoffeeAmountTakenString(
    coffeeUiState: CoffeeUiState,
    selectedAmount: String?
) = buildAnnotatedString {
    append(stringResource(id = R.string.format_coffee_amount_grams, coffeeUiState.amount))
    selectedAmount?.let { amount ->
        append(" ")
        withStyle(style = SpanStyle(color = Color(0xFFDC362E), fontWeight = FontWeight.SemiBold)) {
            append(stringResource(id = R.string.assistant_taken_amount, amount))
        }
    }
}