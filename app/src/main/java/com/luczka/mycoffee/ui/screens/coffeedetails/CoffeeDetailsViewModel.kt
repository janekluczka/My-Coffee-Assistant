package com.luczka.mycoffee.ui.screens.coffeedetails

import androidx.lifecycle.ViewModel
import com.luczka.mycoffee.data.repositories.MyCoffeeDatabaseRepository

class CoffeeDetailsViewModel(
    val coffeeId: Int,
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
): ViewModel() {

}