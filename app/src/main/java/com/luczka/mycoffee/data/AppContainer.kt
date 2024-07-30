package com.luczka.mycoffee.data

import com.luczka.mycoffee.data.repository.FirebaseRepository
import com.luczka.mycoffee.data.repository.MyCoffeeDatabaseRepository

interface AppContainer {
    val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
    val firebaseRepository: FirebaseRepository
}