package com.fintrack.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.fintrack.TestFixtures
import com.fintrack.ui.screen.categories.CategoryListScreen
import com.fintrack.ui.screen.categories.CategoryListUiState
import org.junit.Rule
import org.junit.Test

class CategoryListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun categoryListShowsPreviewAndExistingCategory() {
        composeRule.setContent {
            CategoryListScreen(
                state = CategoryListUiState(
                    isLoading = false,
                    categories = listOf(TestFixtures.groceries),
                    nameInput = "Utilities",
                ),
                onNameChanged = {},
                onIconSelected = {},
                onColorSelected = {},
                onTypeSelected = {},
                onEdit = {},
                onSave = {},
                onDelete = {},
            )
        }

        composeRule.onNodeWithText("Create category").assertIsDisplayed()
        composeRule.onAllNodesWithText("Groceries").assertCountEquals(2)
    }
}
