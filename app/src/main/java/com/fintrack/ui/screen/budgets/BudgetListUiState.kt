package com.fintrack.ui.screen.budgets

import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.BudgetProgress
import com.fintrack.domain.model.Category

data class BudgetListUiState(
    val isLoading: Boolean = true,
    val budgets: List<Budget> = emptyList(),
    val progress: List<BudgetProgress> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isEmpty: Boolean = true,
    val errorMessage: String? = null,
)
