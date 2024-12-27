package com.luczka.mycoffee.ui.components.custom.doubleverticalpager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun DoubleVerticalPagerCard(
    contentAlignment: Alignment,
    style: TextStyle,
    text: Int
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = contentAlignment
    ) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text.toString(),
                    style = style,
                    modifier = Modifier
                        .padding(
                            top = DoubleVerticalPagerCardDefaults.TextTopPadding,
                            bottom = DoubleVerticalPagerCardDefaults.TextBottomPadding,
                            start = DoubleVerticalPagerCardDefaults.TextStartPadding,
                            end = DoubleVerticalPagerCardDefaults.TextEndPadding
                        )
                )
            }
        }
    }
}