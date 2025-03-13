package com.luczka.mycoffee.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.buildSpannedString
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.FavoriteIcon
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.RoastUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeCard(
    modifier: Modifier,
    coffeeUiState: CoffeeUiState,
    imageAspectRatio: Float = 1f / 1f,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val supportingText = buildSpannedString {
        append(stringResource(R.string.format_coffee_amount_grams, coffeeUiState.amount))
        coffeeUiState.roast?.let { roastUiState ->
            append(" • ")
            append(stringResource(roastUiState.stringRes))
        }
        coffeeUiState.process?.let { processUiState ->
            append(" • ")
            append(stringResource(processUiState.stringRes))
        }
    }.toString()

    OutlinedCard(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(imageAspectRatio),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coffeeUiState.roasterOrBrand,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = coffeeUiState.originOrName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = supportingText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (coffeeUiState.isFavourite) {
                FavoriteIcon(tint = MaterialTheme.colorScheme.onSurfaceVariant)
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
        coffeeId = 1,
        originOrName = "salwador finca",
        roasterOrBrand = "monko.",
        amount = "250.0",
        roast = RoastUiState.Medium,
        isFavourite = true
    )
    MyCoffeeTheme(darkTheme = darkTheme) {
        CoffeeCard(
            modifier = Modifier
                .width(250.dp)
                .wrapContentHeight(),
            coffeeUiState = coffeeCardUiState,
            onClick = {},
        )
    }
}