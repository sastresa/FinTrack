package com.fintrack.ui.screen.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.model.TransactionFilter
import com.fintrack.domain.model.TransactionType
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val useCases: FinanceUseCases,
) : ViewModel() {
    private val filter = MutableStateFlow(TransactionFilter())

    val uiState = combine(
        useCases.getTransactions(),
        useCases.getCategories(),
        useCases.getSettings(),
        filter,
    ) { transactions, categories, settings, activeFilter ->
        val filtered = useCases.filterTransactions(transactions, activeFilter)
        TransactionListUiState(
            isLoading = false,
            transactions = filtered,
            groupedTransactions = filtered.groupBy { it.date },
            categories = categories,
            filter = activeFilter,
            currencyCode = settings.currencyCode,
            isEmpty = filtered.isEmpty(),
        )
    }
        .catch { emit(TransactionListUiState(isLoading = false, errorMessage = it.message)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, TransactionListUiState())

    fun onSearchQueryChanged(query: String) {
        filter.update { it.copy(query = query) }
    }

    fun onTypeFilterChanged(type: TransactionType?) {
        filter.update { it.copy(type = type) }
    }

    fun onCategoryFilterChanged(categoryIds: Set<Long>) {
        filter.update { it.copy(categoryIds = categoryIds) }
    }

    fun onDateRangeChanged(startDate: LocalDate?, endDate: LocalDate?) {
        filter.update { it.copy(startDate = startDate, endDate = endDate) }
    }

    fun onDeleteClicked(id: Long) {
        viewModelScope.launch {
            useCases.deleteTransaction(id)
        }
    }
}
