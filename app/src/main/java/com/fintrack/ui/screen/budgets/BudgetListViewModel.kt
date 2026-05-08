package com.fintrack.ui.screen.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.usecase.FinanceUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetListViewModel(
    private val useCases: FinanceUseCases,
) : ViewModel() {
    val uiState = combine(
        useCases.getBudgets(),
        useCases.getTransactions(),
        useCases.getCategories(),
    ) { budgets, transactions, categories ->
        BudgetListUiState(
            isLoading = false,
            budgets = budgets,
            progress = useCases.calculateBudgetProgress(budgets, transactions),
            categories = categories,
            isEmpty = budgets.isEmpty(),
        )
    }
        .catch { emit(BudgetListUiState(isLoading = false, errorMessage = it.message)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, BudgetListUiState())

    fun onDeleteClicked(id: Long) {
        viewModelScope.launch {
            useCases.deleteBudget(id)
        }
    }
}
