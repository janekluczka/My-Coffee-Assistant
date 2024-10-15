package com.luczka.mycoffee.ui.screens.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
@OptIn(ExperimentalMaterial3Api::class)
fun AssistantSummaryCoffeeListItem(
    coffeeUiState: CoffeeUiState,
    selectedAmount: String? = null
) {
    val context = LocalContext.current
    ListItem(
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
                    AsyncImage(
                        model = model,
                        contentDescription = null
                    )
                }
            }
        },
        headlineText = {
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
        },
        supportingText = {
            Text(text = buildCoffeeAmountTakenString(coffeeUiState, selectedAmount))
        }
    )
}

@Composable
private fun buildCoffeeAmountTakenString(
    coffeeUiState: CoffeeUiState,
    selectedAmount: String?
) = buildAnnotatedString {
    append(stringResource(id = R.string.coffee_parameters_amount_with_unit, coffeeUiState.amount.toString()))
    selectedAmount?.let { amount ->
        append(" ")
        withStyle(style = SpanStyle(color = Color(0xFFDC362E), fontWeight = FontWeight.SemiBold)) {
            append(stringResource(id = R.string.assistant_taken_amount, amount))
        }
    }
}