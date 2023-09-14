package com.luczka.mycoffee.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

fun Bitmap.createSquareBitmap(): Bitmap {
    return if (this.width >= this.height) {
        val xOffset = this.width / 2 - this.height / 2
        Bitmap.createBitmap(this, xOffset, 0, this.height, this.height)
    } else {
        val yOffset = this.height / 2 - this.width / 2
        Bitmap.createBitmap(this, 0, yOffset, this.width, this.width)
    }
}

fun Bitmap.createScaledBitmap(width: Int, height: Int, filter: Boolean) : Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, filter)
}

fun Bitmap.compressBitmap(compressionFormat: CompressFormat, quality: Int, outputStream: OutputStream): Boolean {
    return this.compress(compressionFormat, quality, outputStream)
}

object CustomBitmapFactory {
    fun decodeStreamWithOrientation(inputStream: InputStream): Bitmap {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        try {
            val exifInterface = ExifInterface(inputStream)
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