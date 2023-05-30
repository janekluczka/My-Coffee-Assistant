package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalCoffeeCard(
    coffeeUiState: CoffeeUiState,
    imageAspectRatio: Float = 4f / 5f,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        // TODO: Add coffee status item in right top corner
        if (coffeeUiState.bitmap != null) {
            val model = ImageRequest.Builder(LocalContext.current)
                .data(coffeeUiState.bitmap)
                .crossfade(true)
                .crossfade(1000)
                .build()
            AsyncImage(
                model = model,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                    .fillMaxWidth()
                    .aspectRatio(imageAspectRatio)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                    .fillMaxWidth()
                    .aspectRatio(imageAspectRatio)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_coffee_bean),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = coffeeUiState.name,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = coffeeUiState.brand,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Suppress("SpellCheckingInspection")
@Preview
@Composable
fun VerticalCoffeeCardPreview() {
    VerticalCoffeeCard(
        CoffeeUiState(
            name = "salwador finca",
            brand = "monko."
        ),
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight()
    ) {

    }
}
