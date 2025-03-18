package com.luczka.mycoffee.di

import com.luczka.mycoffee.domain.repositories.FirebaseRepository
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.domain.usecases.DeleteBrewUseCase
import com.luczka.mycoffee.domain.usecases.DeleteCoffeeUseCase
import com.luczka.mycoffee.domain.usecases.GetAllBrewsFlowUseCase
import com.luczka.mycoffee.domain.usecases.GetAllCoffeesFlowUseCase
import com.luczka.mycoffee.domain.usecases.GetBrewFlowUseCase
import com.luczka.mycoffee.domain.usecases.GetCoffeeFlowUseCase
import com.luczka.mycoffee.domain.usecases.GetCurrentCoffeesUseCase
import com.luczka.mycoffee.domain.usecases.GetMethodsUseCase
import com.luczka.mycoffee.domain.usecases.GetRecentlyAddedBrewsFlowUseCase
import com.luczka.mycoffee.domain.usecases.GetRecentlyAddedCoffeesFlowUseCase
import com.luczka.mycoffee.domain.usecases.GetRecipesUseCase
import com.luczka.mycoffee.domain.usecases.UpdateCoffeeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    fun providesGetCoffeeFlowUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): GetCoffeeFlowUseCase {
        return GetCoffeeFlowUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesGetAllCoffeesFlowUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): GetAllCoffeesFlowUseCase {
        return GetAllCoffeesFlowUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesGetRecentlyAddedCoffeesFlowUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): GetRecentlyAddedCoffeesFlowUseCase {
        return GetRecentlyAddedCoffeesFlowUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesGetCurrentCoffeesUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): GetCurrentCoffeesUseCase {
        return GetCurrentCoffeesUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesUpdateCoffeeUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): UpdateCoffeeUseCase {
        return UpdateCoffeeUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesDeleteCoffeeUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): DeleteCoffeeUseCase {
        return DeleteCoffeeUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesGetBrewFlowUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): GetBrewFlowUseCase {
        return GetBrewFlowUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesGetAllBrewsFlowUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): GetAllBrewsFlowUseCase {
        return GetAllBrewsFlowUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesGetRecentlyAddedBrewsFlowUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): GetRecentlyAddedBrewsFlowUseCase {
        return GetRecentlyAddedBrewsFlowUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesDeleteBrewUseCase(myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository): DeleteBrewUseCase {
        return DeleteBrewUseCase(myCoffeeDatabaseRepository)
    }

    @Provides
    fun providesGetMethodsUseCase(firebaseRepository: FirebaseRepository): GetMethodsUseCase {
        return GetMethodsUseCase(firebaseRepository)
    }

    @Provides
    fun providesGetRecipesUseCase(firebaseRepository: FirebaseRepository): GetRecipesUseCase {
        return GetRecipesUseCase(firebaseRepository)
    }
}