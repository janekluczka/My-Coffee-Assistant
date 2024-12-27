package com.luczka.mycoffee.ui.components.custom.doubleverticalpager

import androidx.annotation.StringRes

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