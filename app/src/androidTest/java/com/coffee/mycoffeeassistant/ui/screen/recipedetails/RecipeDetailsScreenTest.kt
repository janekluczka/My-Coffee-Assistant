package com.coffee.mycoffeeassistant.ui.screen.recipedetails

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.BrewingStepUiState
import com.luczka.mycoffee.ui.models.RecipeUiState
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsAction
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsScreen
import com.luczka.mycoffee.ui.screens.recipedetails.RecipeDetailsUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecipeDetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var sampleUiState: RecipeDetailsUiState

    @Before
    fun setup() {
        val sampleRecipe = RecipeUiState(
            title = "Coffee Recipe",
            author = "Coffee Influencer",
            steps = listOf(
                BrewingStepUiState(number = 1, description = "Measure and grind coffee (15 g)", time = null),
                BrewingStepUiState(number = 2, description = "Heat water", time = null),
                BrewingStepUiState(number = 3, description = "Brew coffee", time = "2-3 min"),
            ),
            youtubeId = "123456789",
            videoUrl = "https://www.example.com/",
        )

        sampleUiState = RecipeDetailsUiState(
            recipe = sampleRecipe,
            showOpenYouTubeDialog = false
        )
    }

    @Test
    fun recipeDetailsScreen_displaysTitleAndAuthor() {
        composeTestRule.setContent {
            RecipeDetailsScreen(
                uiState = sampleUiState,
                onAction = {}
            )
        }

        composeTestRule.onAllNodesWithText(sampleUiState.recipe.title).assertCountEquals(2)
        composeTestRule.onNodeWithText(sampleUiState.recipe.author).assertIsDisplayed()
    }

    @Test
    fun recipeDetailsScreen_displaysBrewingSteps() {
        composeTestRule.setContent {
            RecipeDetailsScreen(
                uiState = sampleUiState,
                onAction = {}
            )
        }

        sampleUiState.recipe.steps.forEach { step ->
            composeTestRule.onNodeWithText(step.number.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithText(step.description).assertIsDisplayed()
            step.time?.let { composeTestRule.onNodeWithText(it).assertIsDisplayed() }
        }
    }

    @Test
    fun recipeDetailsScreen_backButtonTriggersAction() {
        var actionTriggered: RecipeDetailsAction? = null

        composeTestRule.setContent {
            RecipeDetailsScreen(
                uiState = sampleUiState,
                onAction = { actionTriggered = it }
            )
        }

        val backButtonDescription = composeTestRule.activity.getString(R.string.icon_description_back)

        composeTestRule.onNodeWithContentDescription(backButtonDescription).performClick()

        assert(actionTriggered == RecipeDetailsAction.NavigateUp)
    }

    @Test
    fun recipeDetailsScreen_youtubeButtonTriggersDialogAction() {
        var actionTriggered: RecipeDetailsAction? = null
        lateinit var youTubeButtonDescription: String

        composeTestRule.setContent {
            youTubeButtonDescription = stringResource(R.string.icon_description_youtube)

            RecipeDetailsScreen(
                uiState = sampleUiState,
                onAction = { actionTriggered = it }
            )
        }

        composeTestRule.onNodeWithContentDescription(youTubeButtonDescription).performClick()

        assert(actionTriggered == RecipeDetailsAction.ShowOpenYouTubeDialog)
    }

    @Test
    fun recipeDetailsScreen_leaveApplicationDialog_displaysWhenOpen() {
        val uiStateWithDialog = sampleUiState.copy(showOpenYouTubeDialog = true)

        val dialogTitle = composeTestRule.activity.getString(R.string.dialog_open_youtube_title)
        val dialogText = composeTestRule.activity.getString(R.string.dialog_open_youtube_text)

        composeTestRule.setContent {
            RecipeDetailsScreen(
                uiState = uiStateWithDialog,
                onAction = {}
            )
        }

        composeTestRule.onNodeWithText(dialogTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(dialogText).assertIsDisplayed()
    }

    @Test
    fun recipeDetailsScreen_leaveApplicationDialog_negativeButtonTriggersAction() {
        var actionTriggered: RecipeDetailsAction? = null
        val uiStateWithDialog = sampleUiState.copy(showOpenYouTubeDialog = true)
        val confirmButtonText = composeTestRule.activity.getString(R.string.action_cancel)

        composeTestRule.setContent {
            RecipeDetailsScreen(
                uiState = uiStateWithDialog,
                onAction = { actionTriggered = it }
            )
        }

        composeTestRule.onNodeWithText(confirmButtonText).performClick()

        assert(actionTriggered == RecipeDetailsAction.OnLeaveApplicationClicked)
    }

    @Test
    fun recipeDetailsScreen_leaveApplicationDialog_positiveButtonTriggersAction() {
        var actionTriggered: RecipeDetailsAction? = null
        val uiStateWithDialog = sampleUiState.copy(showOpenYouTubeDialog = true)
        val dismissButtonText = composeTestRule.activity.getString(R.string.action_cancel)

        composeTestRule.setContent {
            RecipeDetailsScreen(
                uiState = uiStateWithDialog,
                onAction = { actionTriggered = it }
            )
        }

        composeTestRule.onNodeWithText(dismissButtonText).performClick()

        assert(actionTriggered == RecipeDetailsAction.OnLeaveApplicationClicked)
    }
}