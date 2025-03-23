package com.luczka.mycoffee.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a receipt for an equipment purchase.
 * Can store either a PDF file or an image file path.
 *
 * @property receiptId The unique ID of the receipt.
 * @property pdfFile The file path to the PDF version of the receipt, if applicable.
 * @property imageFile The file path to the image version of the receipt, if applicable.
 */
@Entity
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
    val receiptId: Long,
    val pdfFile: String?,
    val imageFile: String?
)