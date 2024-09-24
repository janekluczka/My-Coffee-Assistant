package com.luczka.mycoffee.ui.components.custom

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.luczka.mycoffee.ui.components.buttons.KeyboardIconButton
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import com.luczka.mycoffee.util.toDigitList
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private const val beyondViewportPageCount = 25

// TODO Add resizing with current density
@Composable
fun DoubleVerticalPager(
    leftPagerItems: List<Int>,
    rightPagerItems: List<Int>,
    leftPagerPage: Int,
    rightPagerPage: Int,
    separator: String,
    onUpdateLeftPager: (Int) -> Unit,
    onUpdateRightPager: (Int) -> Unit,
    onShowInputDialog: (() -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()

    val leftPagerState = rememberPagerState(initialPage = leftPagerPage, pageCount = { leftPagerItems.size })
    val rightPagerState = rememberPagerState(initialPage = rightPagerPage, pageCount = { rightPagerItems.size })

    val leftPagerFling = PagerDefaults.flingBehavior(
        state = leftPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(leftPagerItems.size)
    )
    val rightPagerFling = PagerDefaults.flingBehavior(
        state = rightPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(rightPagerItems.size)
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
            onUpdateLeftPager(page)
        }
    }

    LaunchedEffect(rightPagerState) {
        snapshotFlow { rightPagerState.currentPage }.collect { page ->
            onUpdateRightPager(page)
        }
    }

    Box(contentAlignment = Alignment.BottomStart) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            VerticalPager(
                state = leftPagerState,
                pageSize = PageSize.Fixed(80.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .weight(1f),
                beyondViewportPageCount = beyondViewportPageCount,
                flingBehavior = leftPagerFling,
                key = { it },
                contentPadding = PaddingValues(vertical = 40.dp),
                pageSpacing = 8.dp,
                reverseLayout = true,
            ) { page ->
                PagerCard(
                    pagerState = leftPagerState,
                    page = page,
                    contentAlignment = Alignment.CenterEnd,
                    text = leftPagerItems[page]
                )
            }
            Text(
                text = separator,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            VerticalPager(
                state = rightPagerState,
                pageSize = PageSize.Fixed(80.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .weight(1f),
                beyondViewportPageCount = beyondViewportPageCount,
                flingBehavior = rightPagerFling,
                key = { it },
                contentPadding = PaddingValues(vertical = 40.dp),
                pageSpacing = 8.dp,
                reverseLayout = true,
            ) { page ->
                PagerCard(
                    pagerState = rightPagerState,
                    page = page,
                    contentAlignment = Alignment.CenterStart,
                    text = rightPagerItems[page]
                )
            }
        }
        if (onShowInputDialog != null) {
            KeyboardIconButton(
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp),
                onClick = onShowInputDialog
            )
        }
    }
}

@Composable
private fun PagerCard(
    pagerState: PagerState,
    page: Int,
    contentAlignment: Alignment,
    text: Int
) {
    Box(
        modifier = Modifier
            .heightIn(min = 80.dp)
            .fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        Card(
            modifier = Modifier
                .graphicsLayer {
                    val currentPage = pagerState.currentPage
                    val currentPageOffsetFraction = pagerState.currentPageOffsetFraction
                    val pageOffset = ((currentPage - page) + currentPageOffsetFraction).absoluteValue
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                },
        ) {
            val numbers = text.toDigitList()
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                numbers.forEach { number ->
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "0",
                            modifier = Modifier,
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.Transparent
                        )
                        Text(
                            text = number.toString(),
                            modifier = Modifier,
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DoubleVerticalPagerPreview() {
    MyCoffeeTheme {
        DoubleVerticalPager(
            leftPagerPage = 10,
            rightPagerPage = 0,
            separator = ".",
            onUpdateLeftPager = {},
            onUpdateRightPager = {},
            leftPagerItems = (1..99).toList(),
            rightPagerItems = (1..9).toList(),
            onShowInputDialog = {}
        )
    }
}