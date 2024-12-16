package com.luczka.mycoffee.ui.components.chips

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipBorder
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoffeeFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = FilterChipDefaults.shape,
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors(
        disabledContainerColor = Color.Transparent,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledSelectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
    ),
    elevation: SelectableChipElevation? = FilterChipDefaults.filterChipElevation(),
    border: SelectableChipBorder? = FilterChipDefaults.filterChipBorder(
        disabledBorderColor = MaterialTheme.colorScheme.outline,
        disabledSelectedBorderColor = Color.Transparent
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        modifier = modifier,
        enabled = enabled,
//        leadingIcon = {
//            Box(
//                modifier = Modifier.animateContentSize(
//                    animationSpec = keyframes { durationMillis = 200 }
//                )
//            ) {
//                if (selected) {
//                    CheckIcon(modifier = Modifier.size(FilterChipDefaults.IconSize))
//                }
//            }
//        },
        trailingIcon = trailingIcon,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource
    )
}