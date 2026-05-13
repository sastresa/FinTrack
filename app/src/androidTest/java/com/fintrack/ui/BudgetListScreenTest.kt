package com.fintrack.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.fintrack.TestFixtures
import com.fintrack.ui.screen.budgets.BudgetListScreen
import com.fintrack.ui.screen.budgets.BudgetListUiState
import org.junit.Rule
import org.junit.Test

class BudgetListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun budgetScreenRendersAddForm() {
        composeRule.setContent {
            BudgetListScreen(
                state = BudgetListUiState(
                    isLoading = false,
                    categories = listOf(TestFixtures.groceries),
                    monthInput = "2026-05",
                ),
                onCategorySelected = {},
                onMonthChanged = {},
                onLimitAmountChanged = {},
                onSave = {},
                onDelete = {},
            )
        }

        composeRule.onNodeWithText("Add budget").assertIsDisplayed()
        composeRule.onNodeWithText("Limit amount").assertIsDisplayed()
        composeRule.onNodeWithText("Save budget").assertIsDisplayed()
    }
}
