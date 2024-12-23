package com.luczka.mycoffee.ui.screens.recipes

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.ui.models.MethodUiState

@Composable
fun RecipesMethodInfoDialog(
    method: MethodUiState,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = "What is ${method.name}?")
        },
        text = {
            Text(text = method.description)
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