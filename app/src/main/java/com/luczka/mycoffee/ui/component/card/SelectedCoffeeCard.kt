package com.luczka.mycoffee.ui.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.model.CoffeeUiState
import java.io.File

@Composable
fun SelectedCoffeeCard(
    coffeeUiState: CoffeeUiState,
    selectedAmount: String? = null
) {
    val context = LocalContext.current
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
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
            Column(
                modifier = Modifier.padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 24.dp,
                    bottom = 8.dp
                )
            ) {
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
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(
                            id = R.string.coffee_parameters_amount_with_unit,
                            coffeeUiState.amount.toString(),
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    selectedAmount?.let { amount ->
                        Text(
                            text = stringResource(
                                id = R.string.assistant_taken_amount,
                                amount
                            ),
                            color = Color(0xFFDC362E),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}