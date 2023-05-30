package com.coffee.mycoffeeassistant

import android.app.Application
import com.coffee.mycoffeeassistant.data.AppContainer
import com.coffee.mycoffeeassistant.data.AppDataContainer

class MyCoffeeAssistantApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}