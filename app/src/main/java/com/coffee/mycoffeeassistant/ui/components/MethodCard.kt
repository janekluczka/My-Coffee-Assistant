package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffee.mycoffeeassistant.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodCard(index: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .height(height = 120.dp)
            .fillMaxWidth()
    ) {
        Box(Modifier.fillMaxSize()) {
            val contrast = 1f // 0f..10f (1 should be default)
            val brightness = -47f // -255f..255f (0 should be default)
            val colorMatrix = floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
            // TODO: Modifier.blur() works only on Android 12+
            Image(
                painter = painterResource(id = R.drawable.img_making_coffee),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix)),
                modifier = Modifier
                    .fillMaxSize()
                    .blur(
                        radiusX = 5.dp,
                        radiusY = 5.dp,
                    )
            )
            Text(
                text = "Method $index",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 40.sp,
            )
        }
    }
}

@Preview
@Composable
fun MethodCardPreview() {
    MethodCard(index = 0) {}
}