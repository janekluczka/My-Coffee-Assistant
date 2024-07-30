package com.luczka.mycoffee.data

import android.content.Context
import com.luczka.mycoffee.data.database.MyCoffeeDatabase
import com.luczka.mycoffee.data.repository.FirebaseRepository
import com.luczka.mycoffee.data.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.data.repository.OfflineMyCoffeeDatabaseRepository
import com.luczka.mycoffee.data.repository.OnlineFirebaseRepository

class AppDataContainer(private val context: Context) : AppContainer {

    override val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository by lazy {
        val myCoffeeDao = MyCoffeeDatabase.getDatabase(context).myCoffeeDao()
        OfflineMyCoffeeDatabaseRepository(myCoffeeDao = myCoffeeDao)
    }

    override val firebaseRepository: FirebaseRepository by lazy {
        OnlineFirebaseRepository()
    }

}