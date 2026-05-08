package com.fintrack.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.fintrack.ui.screen.editor.TransactionEditorScreen
import com.fintrack.ui.screen.editor.TransactionEditorUiState
import org.junit.Rule
import org.junit.Test

class TransactionEditorScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun editorShowsValidationMessages() {
        composeRule.setContent {
            TransactionEditorScreen(
                state = TransactionEditorUiState(
                    fieldErrors = mapOf(
                        "title" to "Title is required",
                        "amount" to "Amount must be greater than 0",
                    ),
                ),
                onTitleChanged = {},
                onAmountChanged = {},
                onTypeSelected = {},
                onCategorySelected = {},
                onDateSelected = {},
                onNotesChanged = {},
                onRecurringChanged = {},
                onSave = {},
                onCancel = {},
            )
        }

        composeRule.onNodeWithText("Title is required").assertIsDisplayed()
        composeRule.onNodeWithText("Amount must be greater than 0").assertIsDisplayed()
    }
}
