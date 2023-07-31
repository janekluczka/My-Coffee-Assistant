package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.coffee.mycoffeeassistant.ui.model.components.DropdownMenuItemUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <T> CustomExposedDropdownMenu(
    value: String,
    label: @Composable (() -> Unit)?,
    menuItems: List<DropdownMenuItemUiState<T>>,
    onItemSelected: (T) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = menuExpanded,
        onExpandedChange = { /* Change handled by interactionSource */ },
    ) {
        ClickableOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = value,
            maxLines = 1,
            label = label,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
            onClick = { menuExpanded = !menuExpanded }
        )
        ExposedDropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
        ) {
            menuItems.forEach { item ->
                val text = when {
                    item.stringResource != null -> stringResource(id = item.stringResource)
                    item.description != null -> item.description
                    else -> ""
                }
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        onItemSelected(item.id)
                        menuExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}