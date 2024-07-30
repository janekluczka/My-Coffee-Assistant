package com.luczka.mycoffee.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luczka.mycoffee.MyCoffeeApplication
import com.luczka.mycoffee.ui.screen.assistant.AssistantViewModel
import com.luczka.mycoffee.ui.screen.coffeedetails.CoffeeDetailsViewModel
import com.luczka.mycoffee.ui.screen.coffeeinput.CoffeeInputViewModel
import com.luczka.mycoffee.ui.screen.coffees.CoffeesViewModel
import com.luczka.mycoffee.ui.screen.history.HistoryViewModel
import com.luczka.mycoffee.ui.screen.historydetails.HistoryDetailsViewModel
import com.luczka.mycoffee.ui.screen.home.HomeViewModel
import com.luczka.mycoffee.ui.screen.methods.MethodsViewModel
import com.luczka.mycoffee.ui.screen.recipedetails.RecipeDetailsViewModel
import com.luczka.mycoffee.ui.screen.recipes.RecipesViewModel

object MyCoffeeViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                myCoffeeDatabaseRepository = myCoffeeApplication().container.myCoffeeDatabaseRepository
            )
        }
        initializer {
            AssistantViewModel(
                myCoffeeDatabaseRepository = myCoffeeApplication().container.myCoffeeDatabaseRepository
            )
        }
        initializer {
            HistoryViewModel(
                myCoffeeDatabaseRepository = myCoffeeApplication().container.myCoffeeDatabaseRepository
            )
        }
        initializer {
            CoffeesViewModel(
                myCoffeeDatabaseRepository = myCoffeeApplication().container.myCoffeeDatabaseRepository
            )
        }
        initializer {
            CoffeeInputViewModel(
                myCoffeeDatabaseRepository = myCoffeeApplication().container.myCoffeeDatabaseRepository
            )
        }
        initializer {
            MethodsViewModel(
                firebaseRepository = myCoffeeApplication().container.firebaseRepository
            )
        }
    }

    fun historyDetailsViewModelFactory(brewId: Int): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                HistoryDetailsViewModel(
                    brewId = brewId,
                    myCoffeeDatabaseRepository = myCoffeeApplication().container.myCoffeeDatabaseRepository
                )
            }
        }
    }

    fun coffeeInputViewModelFactory(coffeeId: Int?): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                CoffeeInputViewModel(
                    coffeeId = coffeeId,
                    myCoffeeDatabaseRepository = myCoffeeApplication().container.myCoffeeDatabaseRepository
                )
            }
        }
    }

    fun coffeeDetailsViewModelFactory(coffeeId: Int): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                CoffeeDetailsViewModel(
                    coffeeId = coffeeId,
                    myCoffeeDatabaseRepository = myCoffeeApplication().container.myCoffeeDatabaseRepository
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

    fun recipeDetailsViewModelFactory(recipeId: String): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                RecipeDetailsViewModel(
                    recipeId = recipeId,
                    firebaseRepository = myCoffeeApplication().container.firebaseRepository
                )
            }
        }
    }
}

fun CreationExtras.myCoffeeApplication(): MyCoffeeApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyCoffeeApplication