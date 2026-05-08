package com.fintrack.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.fintrack.TestFixtures
import com.fintrack.domain.model.TransactionFilter
import com.fintrack.ui.screen.transactions.TransactionListScreen
import com.fintrack.ui.screen.transactions.TransactionListUiState
import org.junit.Rule
import org.junit.Test

class TransactionListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun transactionListRendersItems() {
        composeRule.setContent {
            TransactionListScreen(
                state = TransactionListUiState(
                    isLoading = false,
                    transactions = listOf(TestFixtures.transaction(1, title = "Coffee")),
                    groupedTransactions = mapOf(TestFixtures.transaction(1).date to listOf(TestFixtures.transaction(1, title = "Coffee"))),
                    categories = listOf(TestFixtures.groceries),
                    filter = TransactionFilter(),
                    isEmpty = false,
                ),
                onSearchQueryChanged = {},
                onTypeFilterChanged = {},
                onTransactionSelected = {},
                onDelete = {},
            )
        }

        composeRule.onNodeWithText("Coffee").assertIsDisplayed()
    }
}
