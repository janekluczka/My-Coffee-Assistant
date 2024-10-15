package com.luczka.mycoffee.ui.components.custom

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.luczka.mycoffee.ui.components.icons.KeyboardIcon
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import com.luczka.mycoffee.ui.theme.MyCoffeeTypography
import kotlinx.coroutines.launch

@Suppress("MayBeConstant")
object DoubleVerticalPagerDefaults {
    val BeyondViewportPageCount = 25
    val TextTopPadding = 8.dp
    val TextBottomPadding = 8.dp
    val PageSpacing = 8.dp
}

@Composable
fun DoubleVerticalPager(
    leftPagerItems: List<Int>,
    rightPagerItems: List<Int>,
    leftPagerPage: Int,
    rightPagerPage: Int,
    separator: String,
    textStyle: TextStyle = MaterialTheme.typography.displayLarge.copy(
        fontFamily = MyCoffeeTypography.redditMonoFontFamily
    ),
    pageSpacing: Dp = DoubleVerticalPagerDefaults.PageSpacing,
    onLeftPagerIndexChanged: (Int) -> Unit,
    onRightPagerIndexChanged: (Int) -> Unit,
    onShowInputDialog: (() -> Unit)? = null
) {
    val density = LocalDensity.current

    val coroutineScope = rememberCoroutineScope()

    val leftPagerState = rememberPagerState(
        initialPage = leftPagerPage,
        pageCount = { leftPagerItems.size }
    )
    val rightPagerState = rememberPagerState(
        initialPage = rightPagerPage,
        pageCount = { rightPagerItems.size }
    )

    val leftPagerFling = PagerDefaults.flingBehavior(
        state = leftPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(leftPagerItems.size)
    )
    val rightPagerFling = PagerDefaults.flingBehavior(
        state = rightPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(rightPagerItems.size)
    )

    val textLineHeight = with(density) { textStyle.lineHeight.toDp() }
    val textTopPadding = DoubleVerticalPagerDefaults.TextTopPadding
    val textBottomPadding = DoubleVerticalPagerDefaults.TextBottomPadding

    val cardHeight = textLineHeight + textTopPadding + textBottomPadding

    val pageSize = PageSize.Fixed(cardHeight)

    val pagerSize = 2 * cardHeight + pageSpacing

    val topHalfTextHeight = textLineHeight / 2 + textTopPadding
    val bottomHalfTextHeight = textLineHeight / 2 + textBottomPadding

    val contentPadding = PaddingValues(
        bottom = topHalfTextHeight + pageSpacing,
        top = bottomHalfTextHeight + pageSpacing
    )

    LaunchedEffect(leftPagerPage) {
        if (!leftPagerState.isScrollInProgress) {
            coroutineScope.launch {
                leftPagerState.animateScrollToPage(
                    page = leftPagerPage,
                    animationSpec = tween(500)
                )
            }
        }
    }

    LaunchedEffect(rightPagerPage) {
        if (!rightPagerState.isScrollInProgress) {
            coroutineScope.launch {
                rightPagerState.animateScrollToPage(
                    page = rightPagerPage,
                    animationSpec = tween(500)
                )
            }
        }
    }

    LaunchedEffect(leftPagerState) {
        snapshotFlow { leftPagerState.currentPage }.collect { page ->
            onLeftPagerIndexChanged(page)
        }
    }

    LaunchedEffect(rightPagerState) {
        snapshotFlow { rightPagerState.currentPage }.collect { page ->
            onRightPagerIndexChanged(page)
        }
    }

    Box(contentAlignment = Alignment.BottomStart) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                VerticalPager(
                    state = leftPagerState,
                    pageSize = pageSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(pagerSize)
                        .weight(1f),
                    beyondViewportPageCount = DoubleVerticalPagerDefaults.BeyondViewportPageCount,
                    flingBehavior = leftPagerFling,
                    key = { it },
                    contentPadding = contentPadding,
                    pageSpacing = pageSpacing,
                    reverseLayout = true,
                ) { page ->
                    PagerCard(
                        contentAlignment = Alignment.CenterEnd,
                        style = textStyle,
                        text = leftPagerItems[page]
                    )
                }
                Text(
                    text = separator,
                    style = textStyle,
                    modifier = Modifier
                        .padding(contentPadding)
                        .width(24.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = textStyle.lineHeight,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                VerticalPager(
                    state = rightPagerState,
                    pageSize = pageSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(pagerSize)
                        .weight(1f),
                    beyondViewportPageCount = DoubleVerticalPagerDefaults.BeyondViewportPageCount,
                    flingBehavior = rightPagerFling,
                    key = { it },
                    contentPadding = contentPadding,
                    pageSpacing = pageSpacing,
                    reverseLayout = true,
                ) { page ->
                    PagerCard(
                        contentAlignment = Alignment.CenterStart,
                        style = textStyle,
                        text = rightPagerItems[page]
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pagerSize),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bottomHalfTextHeight)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.0f)
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topHalfTextHeight)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                                )
                            )
                        )
                )
            }
        }
        if (onShowInputDialog != null) {
            IconButton(
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp),
                onClick = onShowInputDialog
            ) {
                KeyboardIcon()
            }
        }
    }
}

@Composable
private fun PagerCard(
    contentAlignment: Alignment,
    style: TextStyle,
    text: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
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
                            top = DoubleVerticalPagerDefaults.TextTopPadding,
                            bottom = DoubleVerticalPagerDefaults.TextBottomPadding,
                            start = 16.dp,
                            end = 16.dp
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun DoubleVerticalPagerPreview() {
    MyCoffeeTheme {
        DoubleVerticalPager(
            leftPagerPage = 0,
            rightPagerPage = 0,
            separator = ".",
            onLeftPagerIndexChanged = {},
            onRightPagerIndexChanged = {},
            leftPagerItems = (1..99).toList(),
            rightPagerItems = (1..9).toList(),
            onShowInputDialog = {}
        )
    }
}