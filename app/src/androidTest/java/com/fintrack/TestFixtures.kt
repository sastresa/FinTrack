package com.fintrack

import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategoryType
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDate

object TestFixtures {
    val groceries = Category(1, "Groceries", "shopping_cart", "mint", CategoryType.EXPENSE)

    fun transaction(
        id: Long,
        title: String = "Transaction $id",
        type: TransactionType = TransactionType.EXPENSE,
        amount: Long = 1000,
        categoryId: Long = groceries.id,
        date: LocalDate = LocalDate.of(2026, 5, 8),
    ) = Transaction(
        id = id,
        type = type,
        amount = Money(amount),
        title = title,
        categoryId = categoryId,
        date = date,
        notes = null,
        isRecurring = false,
        createdAt = Instant.parse("2026-05-08T10:00:00Z"),
        updatedAt = Instant.parse("2026-05-08T10:00:00Z"),
    )
}
