package com.luczka.mycoffee.di

import com.luczka.mycoffee.ui.navigation.MainNavigator
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MainNavigatorEntryPoint {
    fun getMainNavigator(): MainNavigator
}