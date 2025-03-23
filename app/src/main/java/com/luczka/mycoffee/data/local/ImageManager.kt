package com.luczka.mycoffee.data.local

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

class ImageManager(private val context: Context) {

    fun saveImageToInternalStorage(uri: Uri): String? {
        val contentResolver = context.contentResolver
        val filesDir = context.filesDir

        val fileName = getFileNameFromContentResolver(contentResolver, uri) ?: return null
        val destinationFile = File(filesDir, fileName)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return destinationFile.name
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getFileNameFromContentResolver(contentResolver: ContentResolver, uri: Uri): String? {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    return cursor.getString(displayNameIndex)
                }
            }
        }
        return null
    }

    fun deleteImageFromInternalStorage(fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }
}