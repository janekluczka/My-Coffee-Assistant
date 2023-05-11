package com.coffee.mycoffeeassistant.data

import android.content.Context

interface AppContainer {
    val coffeeRepository: CoffeeRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val coffeeRepository: CoffeeRepository by lazy {
        OfflineCoffeeRepository(CoffeeDatabase.getDatabase(context).coffeeDao())
    }
}