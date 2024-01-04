package com.luczka.mycoffee.data

import com.luczka.mycoffee.data.repositories.FirebaseRepository
import com.luczka.mycoffee.data.repositories.MyCoffeeDatabaseRepository

interface AppContainer {
    val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
    val firebaseRepository: FirebaseRepository
}