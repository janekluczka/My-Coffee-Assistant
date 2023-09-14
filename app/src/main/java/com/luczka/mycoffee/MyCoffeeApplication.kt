package com.luczka.mycoffee

import android.app.Application
import com.luczka.mycoffee.data.AppContainer
import com.luczka.mycoffee.data.AppDataContainer

class MyCoffeeApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}