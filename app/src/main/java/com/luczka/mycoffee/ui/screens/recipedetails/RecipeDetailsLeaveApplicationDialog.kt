package com.luczka.mycoffee.ui.screens.recipedetails

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R

@Composable
fun RecipeDetailsLeaveApplicationDialog(
    onNegative: () -> Unit,
    onPositive: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.dialog_open_youtube_title))
        },
        text = {
            Text(text = stringResource(R.string.dialog_open_youtube_text))
        },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.dialog_action_open))
            }
        },
        tonalElevation = 0.dp
    )
}