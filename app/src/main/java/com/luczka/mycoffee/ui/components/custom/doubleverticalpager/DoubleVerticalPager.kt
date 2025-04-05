package com.luczka.mycoffee.ui.components.custom.doubleverticalpager

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.KeyboardIcon
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import com.luczka.mycoffee.ui.theme.MyCoffeeTypography
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Suppress("MayBeConstant")
private object DoubleVerticalPagerDefaults {
    val BeyondViewportPageCount = 25
    val maxFullVisiblePages = 2
    val PageSpacing = 8.dp
    val LineHeightMultiplier = 1.5f
    val ScrollAnimationDuration = 500
}

private object DoubleVerticalPagerCardDefaults {
    val TextTopPadding = 8.dp
    val TextStartPadding = 16.dp
    val TextEndPadding = 16.dp
    val TextBottomPadding = 8.dp
}

@Composable
fun DoubleVerticalPager(
    leftPagerPageIndex: Int,
    leftPagerItems: List<Int>,
    leftPagerItemsTextFormatter: ((Int) -> String)? = null,
    leftPagerBeyondViewportPageCount: Int = DoubleVerticalPagerDefaults.BeyondViewportPageCount,
    rightPagerPageIndex: Int,
    rightPagerItems: List<Int>,
    rightPagerItemsTextFormatter: ((Int) -> String)? = null,
    rightPagerBeyondViewportPageCount: Int = DoubleVerticalPagerDefaults.BeyondViewportPageCount,
    separatorRes: Int,
    textStyle: TextStyle = MaterialTheme.typography.displayLarge,
    userScrollEnabled: Boolean = true,
    maxFullVisiblePages: Int = DoubleVerticalPagerDefaults.maxFullVisiblePages,
    pageSpacing: Dp = DoubleVerticalPagerDefaults.PageSpacing,
    onLeftPagerIndexChanged: (Int) -> Unit = {},
    onRightPagerIndexChanged: (Int) -> Unit = {},
    iconButtonIcon: (@Composable () -> Unit)? = null,
    onIconButtonClick: (() -> Unit) = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val leftPagerState = rememberPagerState(
        initialPage = leftPagerPageIndex,
        pageCount = { leftPagerItems.size }
    )
    val rightPagerState = rememberPagerState(
        initialPage = rightPagerPageIndex,
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

    val textLineHeight = with(LocalDensity.current) {
        val lineHeightSp = if (textStyle.lineHeight != TextUnit.Unspecified) {
            textStyle.lineHeight
        } else {
            textStyle.fontSize * DoubleVerticalPagerDefaults.LineHeightMultiplier
        }
        lineHeightSp.toDp()
    }
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

    LaunchedEffect(leftPagerPageIndex) {
        if (leftPagerPageIndex == leftPagerState.currentPage) return@LaunchedEffect
        if (leftPagerState.isScrollInProgress) return@LaunchedEffect

        coroutineScope.launch {
            leftPagerState.animateScrollToPage(
                page = leftPagerPageIndex,
                animationSpec = tween(DoubleVerticalPagerDefaults.ScrollAnimationDuration)
            )
        }
    }

    LaunchedEffect(rightPagerPageIndex) {
        if (rightPagerPageIndex == rightPagerState.currentPage) return@LaunchedEffect
        if (rightPagerState.isScrollInProgress) return@LaunchedEffect

        coroutineScope.launch {
            rightPagerState.animateScrollToPage(
                page = rightPagerPageIndex,
                animationSpec = tween(DoubleVerticalPagerDefaults.ScrollAnimationDuration)
            )
        }
    }

    LaunchedEffect(leftPagerState) {
        snapshotFlow { leftPagerState.currentPage }
            .distinctUntilChanged()
            .collectLatest { leftPageIndex ->
                Log.d(javaClass.simpleName, "Left pager page collected: $leftPageIndex")
                if (userScrollEnabled) {
                    onLeftPagerIndexChanged(leftPageIndex)
                }
            }
    }

    LaunchedEffect(rightPagerState) {
        snapshotFlow { rightPagerState.currentPage }
            .distinctUntilChanged()
            .collectLatest { rightPageIndex ->
                Log.d(javaClass.simpleName, "Right pager page collected (changed): $rightPageIndex")
                if (userScrollEnabled) {
                    onRightPagerIndexChanged(rightPageIndex)
                }
            }
    }

    Box(
        modifier = Modifier.height(pagerSize),
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                VerticalPager(
                    state = leftPagerState,
                    pageSize = pageSize,
                    modifier = Modifier.wrapContentWidth(),
                    beyondViewportPageCount = leftPagerBeyondViewportPageCount,
                    flingBehavior = leftPagerFling,
                    key = { it },
                    contentPadding = contentPadding,
                    horizontalAlignment = Alignment.End,
                    pageSpacing = pageSpacing,
                    reverseLayout = true,
                    userScrollEnabled = userScrollEnabled,
//                        pageNestedScrollConnection = // TODO: Remove default nested scroll connection
                ) { page ->
                    DoubleVerticalPagerCard(
                        style = textStyle,
                        value = leftPagerItems[page],
                        textFormatter = leftPagerItemsTextFormatter
                    )
                }
            }
            Text(
                text = stringResource(id = separatorRes),
                style = textStyle,
                modifier = Modifier
                    .padding(contentPadding)
                    .width(24.dp),
                textAlign = TextAlign.Center,
                lineHeight = textStyle.lineHeight,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                VerticalPager(
                    state = rightPagerState,
                    pageSize = pageSize,
                    modifier = Modifier.wrapContentWidth(),
                    beyondViewportPageCount = rightPagerBeyondViewportPageCount,
                    flingBehavior = rightPagerFling,
                    key = { it },
                    contentPadding = contentPadding,
                    horizontalAlignment = Alignment.Start,
                    pageSpacing = pageSpacing,
                    reverseLayout = true,
                    userScrollEnabled = userScrollEnabled,
//                        pageNestedScrollConnection = // TODO: Remove default nested scroll connection
                ) { page ->
                    DoubleVerticalPagerCard(
                        style = textStyle,
                        value = rightPagerItems[page],
                        textFormatter = rightPagerItemsTextFormatter
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
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
        iconButtonIcon?.let {
            IconButton(
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp),
                onClick = onIconButtonClick,
                content = it
            )
        }
    }
}

@Composable
private fun DoubleVerticalPagerCard(
    style: TextStyle,
    value: Int,
    textFormatter: ((Int) -> String)? = null,
    textPadding: PaddingValues = PaddingValues(
        top = DoubleVerticalPagerCardDefaults.TextTopPadding,
        bottom = DoubleVerticalPagerCardDefaults.TextBottomPadding,
        start = DoubleVerticalPagerCardDefaults.TextStartPadding,
        end = DoubleVerticalPagerCardDefaults.TextEndPadding
    )
) {
    Card(modifier = Modifier.wrapContentSize()) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(textPadding),
                text = textFormatter?.invoke(value) ?: value.toString(),
                style = style,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun DoubleVerticalPagerPreview() {
    val leftPagerItems = (1..10).toList()
    val rightPagerItems = (1..10).toList()
    MyCoffeeTheme {
        DoubleVerticalPager(
            leftPagerItems = leftPagerItems,
            rightPagerItems = rightPagerItems,
            leftPagerPageIndex = leftPagerItems.lastIndex,
            rightPagerPageIndex = 0,
            separatorRes = R.string.separator_amount,
            textStyle = MaterialTheme.typography.displayLarge.copy(
                fontFamily = MyCoffeeTypography.redditMonoFontFamily,
            )
        )
    }
}

@Preview
@Composable
private fun DoubleVerticalPagerWithButtonPreview() {
    val leftPagerItems = (1..10).toList()
    val rightPagerItems = (1..10).toList()
    MyCoffeeTheme {
        DoubleVerticalPager(
            leftPagerItems = leftPagerItems,
            rightPagerItems = rightPagerItems,
            leftPagerPageIndex = leftPagerItems.lastIndex,
            rightPagerPageIndex = 0,
            separatorRes = R.string.separator_amount,
            iconButtonIcon = { KeyboardIcon() },
            textStyle = MaterialTheme.typography.displayLarge.copy(
                fontFamily = MyCoffeeTypography.redditMonoFontFamily,
            )
        )
    }
}