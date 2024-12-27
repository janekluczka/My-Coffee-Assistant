package com.luczka.mycoffee.ui.screens.brewassistant.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.luczka.mycoffee.ui.components.icons.ArrowDropDownIcon
import com.luczka.mycoffee.ui.components.icons.ArrowDropUpIcon

@Composable
fun BrewAssistantParametersExpandableListItem(
    onClick: () -> Unit,
    index: Int,
    overlineText: String,
    headlineText: String,
    expanded: Boolean,
    expandableContent: @Composable AnimatedVisibilityScope.() -> Unit
) {
    BrewAssistantParametersListItem(
        modifier = Modifier.clickable(onClick = onClick),
        index = index,
        overlineText = overlineText,
        headlineText = headlineText,
        trailingContent = {
            if (expanded) {
                ArrowDropUpIcon()
            } else {
                ArrowDropDownIcon()
            }
        }
    )
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(),
        exit = shrinkVertically(),
        content = expandableContent
    )
}