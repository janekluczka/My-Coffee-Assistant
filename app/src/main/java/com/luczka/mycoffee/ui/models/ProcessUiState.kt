package com.luczka.mycoffee.ui.models

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

/**
 * Represents the different coffee processing methods in the UI layer.
 *
 * @property stringRes The string resource ID for the process name.
 */
enum class ProcessUiState(@StringRes val stringRes: Int) {
    Natural(R.string.process_natural),
    Washed(R.string.process_washed),
    Honey(R.string.process_honey),
    Other(R.string.process_other)
}