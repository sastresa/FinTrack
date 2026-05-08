package com.fintrack.domain.usecase

import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategorySummary
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.MonthlyTrendPoint
import com.fintrack.domain.model.ReportData
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionType
import java.time.YearMonth

class BuildReportDataUseCase(
    private val calculateMonthlySummary: CalculateMonthlySummaryUseCase = CalculateMonthlySummaryUseCase(),
) {
    operator fun invoke(
        transactions: List<Transaction>,
        categories: List<Category>,
        month: YearMonth,
    ): ReportData {
        val summary = calculateMonthlySummary(transactions, month)
        val categoryNames = categories.associate { it.id to it.name }
        val monthExpenses = transactions.filter {
            it.type == TransactionType.EXPENSE && YearMonth.from(it.date) == month
        }
        val totalExpenses = monthExpenses.fold(Money.ZERO) { total, transaction -> total + transaction.amount }
        val breakdown = monthExpenses
            .groupBy { it.categoryId }
            .map { (categoryId, categoryTransactions) ->
                val total = categoryTransactions.fold(Money.ZERO) { acc, transaction -> acc + transaction.amount }
                CategorySummary(
                    categoryId = categoryId,
                    categoryName = categoryNames[categoryId] ?: "Uncategorized",
                    total = total,
                    percentage = if (totalExpenses.minorUnits == 0L) {
                        0.0
                    } else {
                        total.minorUnits.toDouble() / totalExpenses.minorUnits.toDouble()
                    },
                )
            }
            .sortedByDescending { it.total.minorUnits }

        val trend = (5 downTo 0)
            .map { month.minusMonths(it.toLong()) }
            .map { trendMonth ->
                val trendSummary = calculateMonthlySummary(transactions, trendMonth)
                MonthlyTrendPoint(
                    month = trendMonth,
                    income = trendSummary.income,
                    expenses = trendSummary.expenses,
                )
            }

        return ReportData(
            month = month,
            summary = summary,
            expenseBreakdown = breakdown,
            incomeVsExpenseTrend = trend,
        )
    }
}
