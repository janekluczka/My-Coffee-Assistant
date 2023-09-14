package com.luczka.mycoffee.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
fun CoffeeCard(
    modifier: Modifier,
    coffeeUiState: CoffeeUiState,
    imageAspectRatio: Float = 1f / 1f,
    onClick: (CoffeeUiState) -> Unit
) {
    val context = LocalContext.current
    Card(
        onClick = { onClick(coffeeUiState) },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(4.dp),
//            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageAspectRatio)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                // TODO: Add image selection by size
                coffeeUiState.imageFile480x480?.let { imageFile ->
                    val cacheFile = File(context.filesDir, imageFile)
                    val model = ImageRequest.Builder(LocalContext.current)
                        .data(cacheFile)
                        .build()
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = model,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
            ) {
                Text(
                    text = coffeeUiState.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = coffeeUiState.brand,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


//private fun selectImageFileBasedOnSize(
//    coffeeUiState: CoffeeUiState,
//    width: Int,
//    height: Int
//): String {
//    val imageSize360 = 360
//    val imageSize480 = 480
//    val imageSize720 = 720
//    val imageSize960 = 960
//
//    return when {
//        width >= imageSize960 || height >= imageSize960 -> coffeeUiState.imageFile960x960
//        width >= imageSize720 || height >= imageSize720 -> coffeeUiState.imageFile720x720
//        width >= imageSize480 || height >= imageSize480 -> coffeeUiState.imageFile480x480
//        width >= imageSize360 || height >= imageSize360 -> coffeeUiState.imageFile360x360
//        else -> coffeeUiState.imageFile240x240
//    }
//}

@Preview
@Composable
fun LightThemeVerticalCoffeeCardPreview() {
    VerticalCoffeeCardPreview(false)
}

@Preview
@Composable
fun DarkThemeVerticalCoffeeCardPreview() {
    VerticalCoffeeCardPreview(true)
}

@Suppress("SpellCheckingInspection")
@Composable
private fun VerticalCoffeeCardPreview(darkTheme: Boolean) {
    val coffeeCardUiState = CoffeeUiState(
        id = 1,
        name = "salwador finca",
        brand = "monko."
    )
    MyCoffeeTheme(darkTheme = darkTheme) {
        CoffeeCard(
            modifier = Modifier
                .width(150.dp)
                .wrapContentHeight(),
            coffeeUiState = coffeeCardUiState,
            onClick = {},
        )
    }
}