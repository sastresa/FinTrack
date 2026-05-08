package com.fintrack.domain.usecase

import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategoryType
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionFilter
import com.fintrack.domain.model.TransactionInput
import com.fintrack.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainUseCasesTest {
    private val groceries = Category(
        id = 1,
        name = "Groceries",
        iconName = "shopping_cart",
        colorToken = "mint",
        type = CategoryType.EXPENSE,
    )
    private val salary = Category(
        id = 2,
        name = "Salary",
        iconName = "payments",
        colorToken = "blue",
        type = CategoryType.INCOME,
    )

    @Test
    fun parseMoneyConvertsDecimalInputToMinorUnits() {
        val parser = ParseMoneyUseCase()

        assertEquals(Money(12345), parser("123.45").getOrThrow())
        assertEquals(Money(900), parser("9").getOrThrow())
        assertTrue(parser("12.345").isFailure)
        assertTrue(parser("abc").isFailure)
    }

    @Test
    fun validateTransactionInputReportsMissingRequiredFields() {
        val result = ValidateTransactionInputUseCase()(
            TransactionInput(
                id = null,
                title = " ",
                amountText = "0",
                type = TransactionType.EXPENSE,
                categoryId = null,
                date = null,
                notes = "",
                isRecurring = false,
            ),
        )

        assertFalse(result.isValid)
        assertEquals("Title is required", result.fieldErrors["title"])
        assertEquals("Amount must be greater than 0", result.fieldErrors["amount"])
        assertEquals("Category is required", result.fieldErrors["category"])
        assertEquals("Date is required", result.fieldErrors["date"])
    }

    @Test
    fun validateTransactionInputReturnsDraftForValidInput() {
        val result = ValidateTransactionInputUseCase()(
            TransactionInput(
                id = 42,
                title = "Coffee",
                amountText = "4.50",
                type = TransactionType.EXPENSE,
                categoryId = 1,
                date = LocalDate.of(2026, 5, 8),
                notes = "Morning",
                isRecurring = true,
            ),
        )

        assertTrue(result.isValid)
        assertEquals(Money(450), result.draft?.amount)
        assertEquals(42L, result.draft?.id)
        assertEquals("Coffee", result.draft?.title)
    }

    @Test
    fun calculateMonthlySummaryUsesOnlySelectedMonth() {
        val summary = CalculateMonthlySummaryUseCase()(
            transactions = listOf(
                transaction(id = 1, type = TransactionType.INCOME, amount = 500000, date = LocalDate.of(2026, 5, 1), categoryId = salary.id),
                transaction(id = 2, type = TransactionType.EXPENSE, amount = 125000, date = LocalDate.of(2026, 5, 2), categoryId = groceries.id),
                transaction(id = 3, type = TransactionType.EXPENSE, amount = 9900, date = LocalDate.of(2026, 4, 30), categoryId = groceries.id),
            ),
            month = YearMonth.of(2026, 5),
        )

        assertEquals(Money(500000), summary.income)
        assertEquals(Money(125000), summary.expenses)
        assertEquals(Money(375000), summary.balance)
        assertEquals(0.75, summary.savingsRate, 0.0001)
    }

    @Test
    fun filterTransactionsAppliesQueryTypeCategoryAndDateRange() {
        val transactions = listOf(
            transaction(id = 1, title = "Coffee", notes = "Latte", type = TransactionType.EXPENSE, amount = 450, date = LocalDate.of(2026, 5, 8), categoryId = groceries.id),
            transaction(id = 2, title = "Salary", type = TransactionType.INCOME, amount = 500000, date = LocalDate.of(2026, 5, 1), categoryId = salary.id),
            transaction(id = 3, title = "Coffee beans", notes = "Home", type = TransactionType.EXPENSE, amount = 1450, date = LocalDate.of(2026, 4, 30), categoryId = groceries.id),
        )

        val result = FilterTransactionsUseCase()(
            transactions,
            TransactionFilter(
                query = "coffee",
                type = TransactionType.EXPENSE,
                categoryIds = setOf(groceries.id),
                startDate = LocalDate.of(2026, 5, 1),
                endDate = LocalDate.of(2026, 5, 31),
            ),
        )

        assertEquals(listOf(1L), result.map { it.id })
    }

    @Test
    fun calculateBudgetProgressDetectsExceededBudgets() {
        val budget = Budget(
            id = 1,
            categoryId = groceries.id,
            month = YearMonth.of(2026, 5),
            limit = Money(10000),
        )

        val progress = CalculateBudgetProgressUseCase()(
            budgets = listOf(budget),
            transactions = listOf(
                transaction(id = 1, type = TransactionType.EXPENSE, amount = 4000, date = LocalDate.of(2026, 5, 3), categoryId = groceries.id),
                transaction(id = 2, type = TransactionType.EXPENSE, amount = 7000, date = LocalDate.of(2026, 5, 4), categoryId = groceries.id),
                transaction(id = 3, type = TransactionType.EXPENSE, amount = 5000, date = LocalDate.of(2026, 6, 1), categoryId = groceries.id),
            ),
        )

        assertEquals(1, progress.size)
        assertEquals(Money(11000), progress.first().spent)
        assertEquals(Money(-1000), progress.first().remaining)
        assertEquals(1.1, progress.first().percentage, 0.0001)
        assertTrue(progress.first().isExceeded)
    }

    private fun transaction(
        id: Long,
        title: String = "Transaction $id",
        type: TransactionType,
        amount: Long,
        date: LocalDate,
        categoryId: Long,
        notes: String? = null,
    ) = Transaction(
        id = id,
        type = type,
        amount = Money(amount),
        title = title,
        categoryId = categoryId,
        date = date,
        notes = notes,
        isRecurring = false,
        createdAt = Instant.parse("2026-05-08T10:00:00Z"),
        updatedAt = Instant.parse("2026-05-08T10:00:00Z"),
    )
}
