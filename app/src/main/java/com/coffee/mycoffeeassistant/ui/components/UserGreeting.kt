package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserGreeting(text: String = "Welcome!") {
    Text(
        text = text,
        fontSize = 30.sp,
        modifier = Modifier.padding(24.dp)
    )
}

@Preview
@Composable
fun UserGreetingPreview() {
    UserGreeting(text = "Welcome Back!")
}