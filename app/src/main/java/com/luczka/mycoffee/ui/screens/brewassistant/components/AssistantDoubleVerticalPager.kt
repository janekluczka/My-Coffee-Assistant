package com.luczka.mycoffee.ui.screens.brewassistant.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.KeyboardIcon
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import com.luczka.mycoffee.ui.theme.MyCoffeeTypography
import kotlinx.coroutines.launch

data class DoubleVerticalPagerState(
    val leftPagerItems: List<Int>,
    val rightPagerItems: List<Int>,
    val leftPagerPageIndex: Int,
    val rightPagerPageIndex: Int,
    @StringRes val separatorRes: Int,
) {
    fun currentLeftPagerItem(): Int {
        return leftPagerItems[leftPagerPageIndex]
    }

    fun currentRightPagerItem(): Int {
        return rightPagerItems[rightPagerPageIndex]
    }
}

@Suppress("MayBeConstant")
object DoubleVerticalPagerDefaults {
    val BeyondViewportPageCount = 25
    val maxFullVisiblePages = 2
    val PageSpacing = 8.dp
}

object DoubleVerticalPagerCardDefaults {
    val TextTopPadding = 8.dp
    val TextBottomPadding = 8.dp
    val TextStartPadding = 16.dp
    val TextEndPadding = 16.dp
}

@Composable
fun DoubleVerticalPager(
    doubleVerticalPagerState: DoubleVerticalPagerState,
    textStyle: TextStyle = MaterialTheme.typography.displayLarge.copy(
        fontFamily = MyCoffeeTypography.redditMonoFontFamily
    ),
    maxFullVisiblePages: Int = DoubleVerticalPagerDefaults.maxFullVisiblePages,
    pageSpacing: Dp = DoubleVerticalPagerDefaults.PageSpacing,
    onLeftPagerIndexChanged: (Int) -> Unit,
    onRightPagerIndexChanged: (Int) -> Unit,
    onShowInputDialog: (() -> Unit)? = null
) {
    val density = LocalDensity.current

    val coroutineScope = rememberCoroutineScope()

    val leftPagerState = rememberPagerState(
        initialPage = doubleVerticalPagerState.leftPagerPageIndex,
        pageCount = { doubleVerticalPagerState.leftPagerItems.size }
    )
    val rightPagerState = rememberPagerState(
        initialPage = doubleVerticalPagerState.rightPagerPageIndex,
        pageCount = { doubleVerticalPagerState.rightPagerItems.size }
    )

    val leftPagerFling = PagerDefaults.flingBehavior(
        state = leftPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(doubleVerticalPagerState.leftPagerItems.size)
    )
    val rightPagerFling = PagerDefaults.flingBehavior(
        state = rightPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(doubleVerticalPagerState.rightPagerItems.size)
    )

    val textLineHeight = with(density) { textStyle.lineHeight.toDp() }
    val textTopPadding = DoubleVerticalPagerCardDefaults.TextTopPadding
    val textBottomPadding = DoubleVerticalPagerCardDefaults.TextBottomPadding

    val cardHeight = textLineHeight + textTopPadding + textBottomPadding

    val pageSize = PageSize.Fixed(cardHeight)

    val pagerSize = maxFullVisiblePages * (cardHeight + pageSpacing)

    val topHalfTextHeight = textLineHeight / 2 + textTopPadding
    val bottomHalfTextHeight = textLineHeight / 2 + textBottomPadding

    val contentPadding = PaddingValues(
        bottom = topHalfTextHeight + pageSpacing,
        top = bottomHalfTextHeight + pageSpacing
    )

    LaunchedEffect(doubleVerticalPagerState.leftPagerPageIndex) {
        if (!leftPagerState.isScrollInProgress) {
            coroutineScope.launch {
                leftPagerState.animateScrollToPage(
                    page = doubleVerticalPagerState.leftPagerPageIndex,
                    animationSpec = tween(500)
                )
            }
        }
    }

    LaunchedEffect(doubleVerticalPagerState.rightPagerPageIndex) {
        if (!rightPagerState.isScrollInProgress) {
            coroutineScope.launch {
                rightPagerState.animateScrollToPage(
                    page = doubleVerticalPagerState.rightPagerPageIndex,
                    animationSpec = tween(500)
                )
            }
        }
    }

    LaunchedEffect(leftPagerState) {
        snapshotFlow { leftPagerState.currentPage }.collect { pageIndex ->
            onLeftPagerIndexChanged(pageIndex)
        }
    }

    LaunchedEffect(rightPagerState) {
        snapshotFlow { rightPagerState.currentPage }.collect { pageIndex ->
            onRightPagerIndexChanged(pageIndex)
        }
    }

    Box(contentAlignment = Alignment.BottomStart) {
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
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
                    DoubleVerticalPagerCard(
                        contentAlignment = Alignment.CenterEnd,
                        style = textStyle,
                        text = doubleVerticalPagerState.leftPagerItems[page]
                    )
                }
                Text(
                    text = stringResource(id = doubleVerticalPagerState.separatorRes),
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
                    DoubleVerticalPagerCard(
                        contentAlignment = Alignment.CenterStart,
                        style = textStyle,
                        text = doubleVerticalPagerState.rightPagerItems[page]
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
private fun DoubleVerticalPagerCard(
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

@Preview
@Composable
fun DoubleVerticalPagerPreview() {
    MyCoffeeTheme {
        val leftPagerItems = (1..10).toList()
        val rightPagerItems = (1..10).toList()
        DoubleVerticalPager(
            doubleVerticalPagerState = DoubleVerticalPagerState(
                leftPagerItems = leftPagerItems,
                rightPagerItems = rightPagerItems,
                leftPagerPageIndex = leftPagerItems.lastIndex,
                rightPagerPageIndex = 0,
                separatorRes = R.string.separator_amount
            ),
            onLeftPagerIndexChanged = {},
            onRightPagerIndexChanged = {},
            onShowInputDialog = {}
        )
    }
}