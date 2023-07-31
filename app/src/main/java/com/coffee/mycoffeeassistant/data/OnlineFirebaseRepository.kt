package com.coffee.mycoffeeassistant.data

import android.net.Uri
import android.util.Log
import com.coffee.mycoffeeassistant.models.Method
import com.coffee.mycoffeeassistant.models.Methods
import com.coffee.mycoffeeassistant.models.Recipe
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.FirebaseStorage

class OnlineFirebaseRepository : FirebaseRepository {

    companion object {
        private const val TAG = "OnlineFirebaseRepository"
        private const val COLLECTION_METHODS = "Methods"
        private const val COLLECTION_RECIPES = "Recipes"
        private const val FIELD_METHOD = "methodId"
        private const val FIELD_YOUTUBE_ID = "youtubeId"
    }

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance(Firebase.app)
    private val firebaseStorage = FirebaseStorage.getInstance(Firebase.app)

    override fun getMethods(onSuccess: (List<Method>) -> Unit) {
        firebaseFirestore.collection(COLLECTION_METHODS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.firstOrNull()?.toObject<Methods>()?.methods?.let { methods ->
                    val imageReferences = methods.map { it.imageRef }
                    val downloadUrlTask = imageReferences.map { imageRef ->
                        firebaseStorage.getReference(imageRef).downloadUrl
                    }
                    Tasks.whenAllComplete(downloadUrlTask).addOnSuccessListener { results ->
                        results.forEachIndexed { index, result ->
                            if (result.isSuccessful) {
                                val downloadUrl = result.result as Uri
                                methods[index].imageUrl = downloadUrl.toString()
                            }
                        }
                        onSuccess(methods)
                    }
                }
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d(TAG, it) }
            }
    }

    override fun getRecipes(methodId: String, onSuccess: (List<Recipe>) -> Unit) {
        firebaseFirestore.collection(COLLECTION_RECIPES)
            .whereEqualTo(FIELD_METHOD, methodId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.mapNotNull { it.toObject<Recipe>() }.let {
                    onSuccess(it)
                }
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d(TAG, it) }
            }
    }

    override fun getRecipe(youtubeId: String, onSuccess: (Recipe) -> Unit) {
        firebaseFirestore.collection(COLLECTION_RECIPES)
            .whereEqualTo(FIELD_YOUTUBE_ID, youtubeId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.firstOrNull()?.toObject<Recipe>()?.let {
                    onSuccess(it)
                }
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d(TAG, it) }
            }
    }


}