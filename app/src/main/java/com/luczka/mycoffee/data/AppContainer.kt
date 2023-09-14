package com.luczka.mycoffee.data

import android.content.Context

interface AppContainer {
    val coffeeRepository: CoffeeRepository
    val firebaseRepository: FirebaseRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val coffeeRepository: CoffeeRepository by lazy {
        val coffeeDao = CoffeeDatabase.getDatabase(context).coffeeDao()
        OfflineCoffeeRepository(coffeeDao = coffeeDao)
    }
    override val firebaseRepository: FirebaseRepository by lazy {
        OnlineFirebaseRepository()
    }
}