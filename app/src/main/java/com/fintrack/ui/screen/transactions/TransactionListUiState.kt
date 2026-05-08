package com.fintrack.ui.screen.transactions

import com.fintrack.domain.model.Category
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionFilter
import java.time.LocalDate

data class TransactionListUiState(
    val isLoading: Boolean = true,
    val transactions: List<Transaction> = emptyList(),
    val groupedTransactions: Map<LocalDate, List<Transaction>> = emptyMap(),
    val categories: List<Category> = emptyList(),
    val filter: TransactionFilter = TransactionFilter(),
    val isEmpty: Boolean = true,
    val errorMessage: String? = null,
)
