package com.coffee.mycoffeeassistant.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import java.io.IOException

object BitmapUtil {

    fun decodeByteArrayWithOrientation(byteArray: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        try {
            val exifInterface = ExifInterface(byteArray.inputStream())
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val rotationDegrees = getRotationDegrees(orientation)
            return rotateBitmap(bitmap, rotationDegrees)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun getRotationDegrees(orientation: Int): Int {
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        if (degrees != 0) {
            val matrix = android.graphics.Matrix()
            matrix.postRotate(degrees.toFloat())
            val rotatedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            return rotatedBitmap
        }
        return bitmap
    }

}