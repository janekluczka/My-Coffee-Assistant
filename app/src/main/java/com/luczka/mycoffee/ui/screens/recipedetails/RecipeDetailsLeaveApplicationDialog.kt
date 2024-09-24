package com.luczka.mycoffee.ui.screens.recipedetails

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.luczka.mycoffee.R

@Composable
fun RecipeDetailsLeaveApplicationDialog(
    onNegative: () -> Unit,
    onPositive: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = "Open YouTube?")
        },
        text = {
            Text(text = "You are about to open YouTube. Are you sure you want to do this?")
        },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = "Open")
            }
        },
    )
}