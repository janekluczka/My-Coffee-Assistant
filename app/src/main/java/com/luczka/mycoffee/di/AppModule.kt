package com.luczka.mycoffee.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.luczka.mycoffee.data.database.MyCoffeeDao
import com.luczka.mycoffee.data.database.MyCoffeeDatabase
import com.luczka.mycoffee.data.repository.FirebaseRepositoryImpl
import com.luczka.mycoffee.data.repository.MyCoffeeDatabaseRepositoryImpl
import com.luczka.mycoffee.domain.repository.FirebaseRepository
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyCoffeeDatabase(@ApplicationContext context: Context): MyCoffeeDatabase {
        return Room.databaseBuilder(
            context,
            MyCoffeeDatabase::class.java,
            "coffee_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesMyCoffeeDao(myCoffeeDatabase: MyCoffeeDatabase) : MyCoffeeDao {
        return myCoffeeDatabase.myCoffeeDao()
    }

    @Provides
    @Singleton
    fun providesMyCoffeeDatabaseRepository(myCoffeeDao: MyCoffeeDao) : MyCoffeeDatabaseRepository {
        return MyCoffeeDatabaseRepositoryImpl(myCoffeeDao)
    }

    @Provides
    @Singleton
    fun providesFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance(Firebase.app)
    }

    @Provides
    fun providesFirebaseRepository(firebaseFirestore: FirebaseFirestore) : FirebaseRepository {
        return FirebaseRepositoryImpl(firebaseFirestore)
    }

}