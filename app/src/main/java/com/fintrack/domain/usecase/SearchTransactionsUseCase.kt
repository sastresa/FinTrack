package com.fintrack.domain.usecase

import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionFilter

class SearchTransactionsUseCase(
    private val filterTransactions: FilterTransactionsUseCase = FilterTransactionsUseCase(),
) {
    operator fun invoke(
        transactions: List<Transaction>,
        query: String,
    ): List<Transaction> = filterTransactions(transactions, TransactionFilter(query = query))
}
