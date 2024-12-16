package com.luczka.mycoffee.ui.components.listitem

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SwipeableListItem(
    modifier: Modifier = Modifier,
    isRevealed: Boolean,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    actions: @Composable RowScope.() -> Unit,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var actionsRowWidth by remember { mutableFloatStateOf(0f) }

    val contentOffset = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(isRevealed, actionsRowWidth) {
        if (isRevealed) {
            contentOffset.animateTo(-actionsRowWidth)
        } else {
            contentOffset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(modifier = Modifier
            .background(backgroundColor)
            .onSizeChanged {
                actionsRowWidth = it.width.toFloat()
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = contentOffset.value.toInt(),
                        y = 0
                    )
                }
                .pointerInput(true) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                val newOffset = (contentOffset.value + dragAmount)
                                    .coerceIn(
                                        minimumValue = -actionsRowWidth,
                                        maximumValue = 0f
                                    )
                                contentOffset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            if (contentOffset.value < -actionsRowWidth / 2) {
                                coroutineScope.launch {
                                    contentOffset.animateTo(-actionsRowWidth)
                                    onExpanded()
                                }
                            } else {
                                coroutineScope.launch {
                                    contentOffset.animateTo(0f)
                                    onCollapsed()
                                }
                            }
                        }
                    )
                },
            content = content
        )
    }
}