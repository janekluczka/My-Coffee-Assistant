package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeSectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 24.sp,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
    )
}

@Preview
@Composable
fun HomeSectionPreview() {
    HomeSectionHeader(text = "My coffee bags")
}