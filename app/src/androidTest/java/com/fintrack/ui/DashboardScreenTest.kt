package com.fintrack.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.MonthlySummary
import com.fintrack.ui.screen.dashboard.DashboardScreen
import com.fintrack.ui.screen.dashboard.DashboardUiState
import java.time.YearMonth
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun dashboardRendersSummary() {
        val month = YearMonth.of(2026, 5)
        composeRule.setContent {
            DashboardScreen(
                state = DashboardUiState(
                    isLoading = false,
                    month = month,
                    summary = MonthlySummary(
                        month = month,
                        income = Money(500000),
                        expenses = Money(12500),
                        balance = Money(487500),
                        savingsRate = 0.975,
                    ),
                    isEmpty = false,
                ),
                onQuickAdd = {},
                onTransactionSelected = {},
            )
        }

        composeRule.onNodeWithText("Balance").assertIsDisplayed()
        composeRule.onNodeWithText("$4875.00").assertIsDisplayed()
    }
}
