package com.fintrack.ui.screen.dashboard

import com.fintrack.domain.model.CategorySummary
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.MonthlySummary
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.Category
import java.time.YearMonth

data class DashboardUiState(
    val isLoading: Boolean = true,
    val month: YearMonth = YearMonth.now(),
    val summary: MonthlySummary = MonthlySummary(month, Money.ZERO, Money.ZERO, Money.ZERO, 0.0),
    val categoryBreakdown: List<CategorySummary> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val currencyCode: String = "USD",
    val isEmpty: Boolean = true,
    val errorMessage: String? = null,
)
