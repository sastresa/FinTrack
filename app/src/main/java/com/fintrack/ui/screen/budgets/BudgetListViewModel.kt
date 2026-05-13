package com.fintrack.ui.screen.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.BudgetInput
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.YearMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetListViewModel(
    private val useCases: FinanceUseCases,
) : ViewModel() {
    private val formState = MutableStateFlow(BudgetFormState())

    val uiState = combine(
        useCases.getBudgets(),
        useCases.getTransactions(),
        useCases.getCategories(),
        useCases.getSettings(),
        formState,
    ) { budgets, transactions, categories, settings, form ->
        BudgetListUiState(
            isLoading = false,
            budgets = budgets,
            progress = useCases.calculateBudgetProgress(budgets, transactions),
            categories = categories,
            selectedCategoryId = form.selectedCategoryId,
            monthInput = form.monthInput,
            limitAmountText = form.limitAmountText,
            fieldErrors = form.fieldErrors,
            currencyCode = settings.currencyCode,
            isEmpty = budgets.isEmpty(),
        )
    }
        .catch { emit(BudgetListUiState(isLoading = false, errorMessage = it.message)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, BudgetListUiState())

    fun onCategorySelected(categoryId: Long?) {
        formState.update { it.copy(selectedCategoryId = categoryId, fieldErrors = it.fieldErrors - "category") }
    }

    fun onMonthChanged(value: String) {
        formState.update { it.copy(monthInput = value, fieldErrors = it.fieldErrors - "month") }
    }

    fun onLimitAmountChanged(value: String) {
        formState.update { it.copy(limitAmountText = value, fieldErrors = it.fieldErrors - "limit") }
    }

    fun onSaveClicked() {
        val form = formState.value
        val month = parseMonth(form.monthInput)
        val fieldErrors = if (form.monthInput.isBlank()) {
            mapOf("month" to "Month is required")
        } else if (month == null) {
            mapOf("month" to "Month must use YYYY-MM")
        } else {
            emptyMap()
        }
        val validation = useCases.validateBudgetInput(
            BudgetInput(
                id = null,
                categoryId = form.selectedCategoryId,
                month = month,
                limitAmountText = form.limitAmountText,
            ),
        )
        val errors = validation.fieldErrors + fieldErrors
        if (errors.isNotEmpty() || validation.draft == null) {
            formState.update { it.copy(fieldErrors = errors) }
            return
        }

        viewModelScope.launch {
            useCases.addBudget(
                Budget(
                    id = 0,
                    categoryId = validation.draft.categoryId,
                    month = validation.draft.month,
                    limit = validation.draft.limit,
                ),
            )
            formState.update {
                it.copy(
                    limitAmountText = "",
                    fieldErrors = emptyMap(),
                )
            }
        }
    }

    fun onDeleteClicked(id: Long) {
        viewModelScope.launch {
            useCases.deleteBudget(id)
        }
    }

    private fun parseMonth(value: String): YearMonth? =
        runCatching { YearMonth.parse(value.trim()) }.getOrNull()

    private data class BudgetFormState(
        val selectedCategoryId: Long? = null,
        val monthInput: String = YearMonth.now().toString(),
        val limitAmountText: String = "",
        val fieldErrors: Map<String, String> = emptyMap(),
    )
}
