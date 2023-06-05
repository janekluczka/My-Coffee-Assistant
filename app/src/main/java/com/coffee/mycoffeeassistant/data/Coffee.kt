package com.coffee.mycoffeeassistant.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.dateTimeFormatter
import com.coffee.mycoffeeassistant.util.BitmapUtil
import java.time.LocalDate

@Entity(tableName = "coffee_table")
data class Coffee(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var brand: String,
    var currentAmount: Float,
    var startAmount: Float,
    var roast: String,
    var process: String,
    var roastingDate: String,
    var isFavourite: Boolean,
    var imageUri: String,
    var image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Coffee
        if (id != other.id) return false
        if (name != other.name) return false
        if (brand != other.brand) return false
        if (currentAmount != other.currentAmount) return false
        if (startAmount != other.startAmount) return false
        if (roast != other.roast) return false
        if (process != other.process) return false
        if (roastingDate != other.roastingDate) return false
        if (isFavourite != other.isFavourite) return false
        if (imageUri != other.imageUri) return false
        if (!image.contentEquals(other.image)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + brand.hashCode()
        result = 31 * result + currentAmount.hashCode()
        result = 31 * result + startAmount.hashCode()
        result = 31 * result + roast.hashCode()
        result = 31 * result + process.hashCode()
        result = 31 * result + roastingDate.hashCode()
        result = 31 * result + isFavourite.hashCode()
        result = 31 * result + imageUri.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }

    fun toCoffeeUiState(): CoffeeUiState = CoffeeUiState(
        id = id,
        name = name,
        brand = brand,
        currentAmount = currentAmount.toString(),
        startAmount = startAmount.toString(),
        roast = roast,
        process = process,
        roastingDate = LocalDate.parse(roastingDate, dateTimeFormatter),
        isFavourite = isFavourite,
        imageUri = Uri.parse(imageUri),
        image = image,
        bitmap = if (image.isNotEmpty()) BitmapUtil.decodeByteArrayWithOrientation(image) else null
    )
}