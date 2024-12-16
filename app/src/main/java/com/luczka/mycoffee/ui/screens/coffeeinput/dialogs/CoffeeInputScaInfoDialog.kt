package com.luczka.mycoffee.ui.screens.coffeeinput.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CoffeeInputScaInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = "What is SCA score?")
        },
        text = {
            Text(text = "The SCA scoring system is a standardized method used to evaluate the quality of coffee beans. This system scores coffee on a scale from 0 to 100, with higher scores indicating better quality. A score of 80 or above classifies the coffee as 'speciality grade'.")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "OK")
            }
        },
        tonalElevation = 0.dp
    )
}