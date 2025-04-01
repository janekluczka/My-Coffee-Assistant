package com.luczka.mycoffee.ui.components.custom.doubleverticalpager

import androidx.annotation.StringRes

data class DoubleVerticalPagerState(
    val leftPagerPageIndex: Int,
    val rightPagerPageIndex: Int,
    val leftPagerItems: List<Int>,
    val rightPagerItems: List<Int>,
    @StringRes val separatorRes: Int,
) {
    fun currentLeftPagerItem(): Int {
        return leftPagerItems[leftPagerPageIndex]
    }

    fun currentRightPagerItem(): Int {
        return rightPagerItems[rightPagerPageIndex]
    }
}