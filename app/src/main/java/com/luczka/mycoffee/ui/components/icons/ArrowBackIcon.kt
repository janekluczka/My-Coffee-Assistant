package com.luczka.mycoffee.ui.components.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.luczka.mycoffee.R

@Composable
fun ArrowBackIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.ic_arrow_back_24_fill_0_weight_300_grade_0_opticalsize_24),
        contentDescription = stringResource(R.string.icon_description_back),
        tint = tint
    )
}