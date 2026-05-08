package com.fintrack.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.model.TransactionType
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.Clock
import java.time.YearMonth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val useCases: FinanceUseCases,
    clock: Clock = Clock.systemDefaultZone(),
) : ViewModel() {
    private val month = YearMonth.now(clock)

    val uiState = combine(
        useCases.getTransactions(),
        useCases.getCategories(),
    ) { transactions, categories ->
        val summary = useCases.calculateMonthlySummary(transactions, month)
        val report = useCases.buildReportData(transactions, categories, month)
        DashboardUiState(
            isLoading = false,
            month = month,
            summary = summary,
            categoryBreakdown = report.expenseBreakdown,
            recentTransactions = transactions
                .sortedWith(compareByDescending<com.fintrack.domain.model.Transaction> { it.date }.thenByDescending { it.updatedAt })
                .take(5),
            isEmpty = transactions.isEmpty(),
            errorMessage = null,
        )
    }
        .catch { emit(DashboardUiState(isLoading = false, month = month, errorMessage = it.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DashboardUiState(month = month),
        )

    @Suppress("UNUSED_PARAMETER")
    fun onQuickAddClicked(type: TransactionType = TransactionType.EXPENSE) = Unit
}
