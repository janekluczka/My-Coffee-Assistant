package com.luczka.mycoffee.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luczka.mycoffee.MyCoffeeApplication
import com.luczka.mycoffee.ui.screens.brewassistant.AssistantViewModel
import com.luczka.mycoffee.ui.screens.coffeeinput.CoffeeInputViewModel
import com.luczka.mycoffee.ui.screens.home.HomeViewModel
import com.luczka.mycoffee.ui.screens.methods.MethodsViewModel
import com.luczka.mycoffee.ui.screens.mybags.MyBagsViewModel
import com.luczka.mycoffee.ui.screens.recipes.RecipesViewModel

object MyCoffeeViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                coffeeRepository = myCoffeeApplication().container.coffeeRepository
            )
        }
        initializer {
            AssistantViewModel(
                coffeeRepository = myCoffeeApplication().container.coffeeRepository
            )
        }
        initializer {
            MyBagsViewModel(
                coffeeRepository = myCoffeeApplication().container.coffeeRepository
            )
        }
        initializer {
            CoffeeInputViewModel(
                coffeeRepository = myCoffeeApplication().container.coffeeRepository
            )
        }
        initializer {
            MethodsViewModel(
                firebaseRepository = myCoffeeApplication().container.firebaseRepository
            )
        }
    }

    fun coffeeInputViewModelFactory(coffeeId: Int?): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                CoffeeInputViewModel(
                    coffeeId = coffeeId,
                    coffeeRepository = myCoffeeApplication().container.coffeeRepository
                )
            }
        }
    }

    fun methodRecipesViewModelFactory(methodId: String): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                RecipesViewModel(
                    methodId = methodId,
                    firebaseRepository = myCoffeeApplication().container.firebaseRepository
                )
            }
        }
    }
}

fun CreationExtras.myCoffeeApplication(): MyCoffeeApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyCoffeeApplication