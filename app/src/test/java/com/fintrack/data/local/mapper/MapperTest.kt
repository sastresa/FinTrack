package com.fintrack.data.local.mapper

import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategoryType
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperTest {
    @Test
    fun transactionEntityRoundTripsDomainFields() {
        val transaction = Transaction(
            id = 7,
            type = TransactionType.EXPENSE,
            amount = Money(1234),
            title = "Lunch",
            categoryId = 3,
            date = LocalDate.of(2026, 5, 8),
            notes = "Team",
            isRecurring = true,
            createdAt = Instant.parse("2026-05-08T09:00:00Z"),
            updatedAt = Instant.parse("2026-05-08T10:00:00Z"),
        )

        assertEquals(transaction, transaction.toEntity().toDomain())
    }

    @Test
    fun categoryEntityRoundTripsDomainFields() {
        val category = Category(
            id = 2,
            name = "Salary",
            iconName = "payments",
            colorToken = "blue",
            type = CategoryType.INCOME,
        )

        assertEquals(category, category.toEntity().toDomain())
    }

    @Test
    fun budgetEntityRoundTripsDomainFields() {
        val budget = Budget(
            id = 9,
            categoryId = null,
            month = YearMonth.of(2026, 5),
            limit = Money(250000),
        )

        assertEquals(budget, budget.toEntity().toDomain())
    }
}
