package com.coffee.mycoffeeassistant.ui.screens.recipedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.FirebaseRepository
import com.coffee.mycoffeeassistant.ui.model.components.RecipeDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


// TODO: Change background color for iframe in dark mode if possible
/**
 * iframe parameters
 *
 * version: player version
 * enablejsapi:
 *  0 - JavaScript disabled
 *  1 - JavaScript enabled
 * controls:
 *  0 - controls hidden,
 *  1 - controls visible & Flash loaded immidiately
 *  2 - controls visible & Flash loaded when video starts playing
 * fs:
 *  0 - fullscreen button hidden
 *  1 - fullscreen button visible
 *
 * more at: https://developers.google.com/youtube/player_parameters?hl=pl
 */
class RecipeDetailsViewModel(
    private val youtubeId: String,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailsUiState())
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            firebaseRepository.getRecipe(youtubeId = youtubeId) { recipe ->
                val iframeWidth = 640
                val iframeHeight = 360
                val iframeHtml = "<html>" +
                        "<meta name=\"viewport\" content=\"width=${iframeWidth}\">" +
                        "<style>iframe { overflow:hidden; }</style>" +
                        "<body style=\"margin: 0px; padding: 0px\">" +
                        "<iframe " +
                        "id=\"player\" " +
                        "frameborder=\"0\" " +
                        "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
                        "width=\"${iframeWidth}\" " +
                        "height=\"${iframeHeight}\" " +
                        "src=\"https://www.youtube.com/embed/${youtubeId}?version=3&amp;enablejsapi=1&amp;controls=1&amp;fs=0\">" +
                        "</iframe>" +
                        "</body>" +
                        "</html>"
                _uiState.update { recipe.toRecipeDetailsUiState().copy(iframeHtml = iframeHtml) }
            }
        }
    }

}