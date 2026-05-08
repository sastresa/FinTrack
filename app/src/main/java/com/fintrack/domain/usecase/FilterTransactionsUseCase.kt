package com.fintrack.domain.usecase

import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionFilter

class FilterTransactionsUseCase {
    operator fun invoke(
        transactions: List<Transaction>,
        filter: TransactionFilter,
    ): List<Transaction> {
        val query = filter.query.trim().lowercase()
        return transactions
            .asSequence()
            .filter { transaction ->
                query.isEmpty() ||
                    transaction.title.lowercase().contains(query) ||
                    transaction.notes.orEmpty().lowercase().contains(query)
            }
            .filter { transaction -> filter.type == null || transaction.type == filter.type }
            .filter { transaction -> filter.categoryIds.isEmpty() || transaction.categoryId in filter.categoryIds }
            .filter { transaction -> filter.startDate == null || !transaction.date.isBefore(filter.startDate) }
            .filter { transaction -> filter.endDate == null || !transaction.date.isAfter(filter.endDate) }
            .sortedWith(compareByDescending<Transaction> { it.date }.thenByDescending { it.updatedAt })
            .toList()
    }
}
