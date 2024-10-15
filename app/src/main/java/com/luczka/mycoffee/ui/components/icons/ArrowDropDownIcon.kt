package com.luczka.mycoffee.ui.components.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.luczka.mycoffee.R

@Composable
fun ArrowDropDownIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.arrow_drop_down_24px),
        contentDescription = null,
        tint = tint
    )
}