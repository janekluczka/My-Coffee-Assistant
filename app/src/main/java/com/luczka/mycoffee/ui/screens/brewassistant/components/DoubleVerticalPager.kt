package com.luczka.mycoffee.ui.screens.brewassistant.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.luczka.mycoffee.R
import kotlin.math.absoluteValue

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun <T> DoubleVerticalPager(
    leftPagerInitialPage: Int,
    rightPagerInitialPage: Int,
    separator: String,
    onUpdateLeftPager: (Int) -> Unit,
    onUpdateRightPager: (Int) -> Unit,
    leftPagerItems: List<T>,
    rightPagerItems: List<T>,
    onShowInputDialog: (() -> Unit)? = null
) {
    val leftPagerState = rememberPagerState(initialPage = leftPagerInitialPage)
    val rightPagerState = rememberPagerState(initialPage = rightPagerInitialPage)

    val leftPagerFling = PagerDefaults.flingBehavior(
        state = leftPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(leftPagerItems.size)
    )
    val rightPagerFling = PagerDefaults.flingBehavior(
        state = rightPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(9)
    )

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

    val beyondBoundsPageCount = 25

    Box(contentAlignment = Alignment.BottomStart) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            VerticalPager(
                state = leftPagerState,
                pageCount = leftPagerItems.size,
                pageSize = PageSize.Fixed(80.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .weight(1f),
                beyondBoundsPageCount = beyondBoundsPageCount,
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
                pageCount = rightPagerItems.size,
                pageSize = PageSize.Fixed(80.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .weight(1f),
                beyondBoundsPageCount = beyondBoundsPageCount,
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
            Box(modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)) {
                IconButton(onClick = onShowInputDialog) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_keyboard),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> PagerCard(
    pagerState: PagerState,
    page: Int,
    contentAlignment: Alignment,
    text: T
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
                    val pageOffset =
                        ((currentPage - page) + currentPageOffsetFraction).absoluteValue
                    alpha = lerp(
                        start = 0.4f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .heightIn(min = 80.dp)
                    .wrapContentWidth(),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        text = text.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun DoubleVerticalPagerPreview() {
    DoubleVerticalPager(
        leftPagerInitialPage = 0,
        rightPagerInitialPage = 0,
        separator = ".",
        onUpdateLeftPager = {},
        onUpdateRightPager = {},
        leftPagerItems = (1..99).toList().map { it.toString() },
        rightPagerItems = (1..9).toList().map { it.toString() },
        onShowInputDialog = {}
    )
}