package com.luczka.mycoffee.ui.screens.recipedetails

import com.luczka.mycoffee.ui.models.BrewingStepUiState
import com.luczka.mycoffee.ui.models.RecipeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: RecipeDetailsViewModel

    private val sampleRecipe = RecipeUiState(
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

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipeDetailsViewModel(sampleRecipe)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial uiState value`() = runTest {
        val uiState = viewModel.uiState.first()
        assertEquals(sampleRecipe, uiState.recipe)
        assertFalse(uiState.openLeaveApplicationDialog)
    }

    // TODO: Fix
    @Test
    fun `showLeaveApplicationDialog state change`() = runTest {
        viewModel.onAction(RecipeDetailsAction.ShowLeaveApplicationDialog)
        val uiState = viewModel.uiState.first()
        assertTrue(uiState.openLeaveApplicationDialog)
    }

    // TODO: Fix
    @Test
    fun `leaveApplication dialog state reset`() = runTest {
        viewModel.onAction(RecipeDetailsAction.ShowLeaveApplicationDialog)
        viewModel.onAction(RecipeDetailsAction.OnLeaveApplicationClicked)
        val uiState = viewModel.uiState.first()
        assertTrue(uiState.openLeaveApplicationDialog)
    }

    @Test
    fun `NavigateUp event emission`() = runTest {
        viewModel.onAction(RecipeDetailsAction.NavigateUp)
        val event = viewModel.oneTimeEvent.first()
        assertEquals(RecipeDetailsOneTimeEvent.NavigateUp, event)
    }

    @Test
    fun `leaveApplication event emission`() = runTest {
        viewModel.onAction(RecipeDetailsAction.ShowLeaveApplicationDialog)
        viewModel.onAction(RecipeDetailsAction.OnLeaveApplicationClicked)
        val event = viewModel.oneTimeEvent.first()
        assertEquals(RecipeDetailsOneTimeEvent.OpenBrowser(sampleRecipe.videoUrl), event)
    }

    @Test
    fun `Multiple oneTimeEvent emissions`() = runTest {
        viewModel.onAction(RecipeDetailsAction.NavigateUp)
        val event1 = viewModel.oneTimeEvent.first()
        assertEquals(RecipeDetailsOneTimeEvent.NavigateUp, event1)

        viewModel.onAction(RecipeDetailsAction.ShowLeaveApplicationDialog)
        viewModel.onAction(RecipeDetailsAction.OnLeaveApplicationClicked)
        val event2 = viewModel.oneTimeEvent.first()
        assertEquals(RecipeDetailsOneTimeEvent.OpenBrowser(sampleRecipe.videoUrl), event2)
    }

    @Test
    fun `Empty videoUrl`() = runTest {
        val emptyUrlRecipe = sampleRecipe.copy(videoUrl = "")
        viewModel = RecipeDetailsViewModel(emptyUrlRecipe)
        viewModel.onAction(RecipeDetailsAction.ShowLeaveApplicationDialog)
        viewModel.onAction(RecipeDetailsAction.OnLeaveApplicationClicked)
        val event = viewModel.oneTimeEvent.first()
        assertEquals(RecipeDetailsOneTimeEvent.OpenBrowser(""), event)
    }

}