package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.coffee.mycoffeeassistant.R

@Composable
fun BrewAssistantFloatingActionButton() {
    ExtendedFloatingActionButton(
        onClick = { /* TODO: Add navigation to Brew Assistant */ },
        icon = {
            Icon(
                painterResource(R.drawable.ic_baseline_coffee_maker),
                contentDescription = null
            )
        },
        text = { Text(text = "Brew assistant") },
    )
}

@Preview
@Composable
fun BrewAssistantFloatingActionButtonPreview() {
    BrewAssistantFloatingActionButton()
}