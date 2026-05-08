package com.fintrack.domain.usecase

import com.fintrack.domain.model.Category
import com.fintrack.domain.model.Transaction

class GenerateCsvUseCase {
    operator fun invoke(
        transactions: List<Transaction>,
        categories: List<Category>,
    ): String {
        val categoryNames = categories.associate { it.id to it.name }
        val rows = transactions
            .sortedWith(compareByDescending<Transaction> { it.date }.thenByDescending { it.updatedAt })
            .map { transaction ->
                listOf(
                    transaction.id.toString(),
                    transaction.date.toString(),
                    transaction.type.name.lowercase(),
                    transaction.title,
                    "%.2f".format(transaction.amount.minorUnits / 100.0),
                    categoryNames[transaction.categoryId].orEmpty(),
                    transaction.notes.orEmpty(),
                    transaction.isRecurring.toString(),
                ).joinToString(",") { it.csvEscape() }
            }

        return (listOf("id,date,type,title,amount,category,notes,isRecurring") + rows)
            .joinToString("\n")
    }

    private fun String.csvEscape(): String {
        val needsEscaping = any { it == ',' || it == '"' || it == '\n' || it == '\r' }
        val escaped = replace("\"", "\"\"")
        return if (needsEscaping) "\"$escaped\"" else escaped
    }
}
