package com.fintrack.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fintrack.domain.usecase.FinanceUseCases
import com.fintrack.ui.screen.budgets.BudgetListViewModel
import com.fintrack.ui.screen.categories.CategoryListViewModel
import com.fintrack.ui.screen.dashboard.DashboardViewModel
import com.fintrack.ui.screen.reports.ReportsViewModel
import com.fintrack.ui.screen.settings.SettingsViewModel
import com.fintrack.ui.screen.transactions.TransactionListViewModel
import com.fintrack.worker.BackgroundWorkScheduler
import com.fintrack.worker.NoOpBackgroundWorkScheduler

class FinTrackViewModelFactory(
    private val useCases: FinanceUseCases,
    private val backgroundWorkScheduler: BackgroundWorkScheduler = NoOpBackgroundWorkScheduler,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(useCases)
        modelClass.isAssignableFrom(TransactionListViewModel::class.java) -> TransactionListViewModel(useCases)
        modelClass.isAssignableFrom(CategoryListViewModel::class.java) -> CategoryListViewModel(useCases)
        modelClass.isAssignableFrom(BudgetListViewModel::class.java) -> BudgetListViewModel(useCases)
        modelClass.isAssignableFrom(ReportsViewModel::class.java) -> ReportsViewModel(useCases)
        modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(useCases, backgroundWorkScheduler)
        else -> error("Unknown ViewModel class: ${modelClass.name}")
    } as T
}
