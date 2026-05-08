package com.fintrack.domain.model

import java.time.YearMonth

data class MonthlySummary(
    val month: YearMonth,
    val income: Money,
    val expenses: Money,
    val balance: Money,
    val savingsRate: Double,
)

data class CategorySummary(
    val categoryId: Long,
    val categoryName: String,
    val total: Money,
    val percentage: Double,
)

data class MonthlyTrendPoint(
    val month: YearMonth,
    val income: Money,
    val expenses: Money,
)

data class ReportData(
    val month: YearMonth,
    val summary: MonthlySummary,
    val expenseBreakdown: List<CategorySummary>,
    val incomeVsExpenseTrend: List<MonthlyTrendPoint>,
)

data class BudgetProgress(
    val budget: Budget,
    val spent: Money,
    val remaining: Money,
    val percentage: Double,
    val isExceeded: Boolean,
)
