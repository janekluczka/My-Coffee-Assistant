package com.luczka.mycoffee.data.repositories

import android.content.Context
import android.util.Log
import com.luczka.mycoffee.data.mappers.toModel
import com.luczka.mycoffee.data.remote.FirebaseService
import com.luczka.mycoffee.data.remote.model.FirebaseServiceException
import com.luczka.mycoffee.domain.models.ApiError
import com.luczka.mycoffee.domain.models.CategoryModel
import com.luczka.mycoffee.domain.models.RecipeModel
import com.luczka.mycoffee.domain.repositories.FirebaseRepository

class FirebaseRepositoryImpl(
    context: Context,
    private val firebaseService: FirebaseService
) : FirebaseRepository {

    private val localeCode: String = context.resources.configuration.locales[0].language

    override suspend fun getCategories(): Result<List<CategoryModel>> {
        return safeFirebaseServiceRequest {
            val categories = firebaseService
                .getCategories()
                .toModel(localeCode)
            Result.success(categories)
        }
    }

    override suspend fun getRecipes(methodId: String): Result<List<RecipeModel>> {
        return safeFirebaseServiceRequest {
            val recipes = firebaseService
                .getRecipesDto(methodId)
                .toModel()
            Result.success(recipes)
        }
    }

    private suspend fun <T> safeFirebaseServiceRequest(block: suspend () -> Result<T>): Result<T> {
        return try {
            Log.d(javaClass.simpleName, block.toString())
            block()
        } catch (exception: FirebaseServiceException) {
            exception.message?.let { Log.d(javaClass.simpleName, it) }
            Result.failure(exception.toModel())
        } catch (exception: Exception) {
            exception.message?.let { Log.d(javaClass.simpleName, it) }
            Result.failure(ApiError.UnknownError)
        }
    }
}