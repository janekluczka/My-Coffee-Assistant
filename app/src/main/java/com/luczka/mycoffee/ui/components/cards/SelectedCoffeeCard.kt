package com.luczka.mycoffee.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.CoffeeUiState
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
            Surface(
                modifier = Modifier.size(80.dp),
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
                        id = R.string.format_coffee_name_coma_brand,
                        coffeeUiState.originOrName,
                        coffeeUiState.roasterOrBrand
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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