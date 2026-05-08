package com.fintrack.domain.usecase

import com.fintrack.domain.model.Money
import com.fintrack.domain.model.MonthlySummary
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionType
import java.time.YearMonth

class CalculateMonthlySummaryUseCase {
    operator fun invoke(
        transactions: List<Transaction>,
        month: YearMonth,
    ): MonthlySummary {
        val monthly = transactions.filter { YearMonth.from(it.date) == month }
        val income = monthly
            .filter { it.type == TransactionType.INCOME }
            .fold(Money.ZERO) { total, transaction -> total + transaction.amount }
        val expenses = monthly
            .filter { it.type == TransactionType.EXPENSE }
            .fold(Money.ZERO) { total, transaction -> total + transaction.amount }
        val balance = income - expenses
        val savingsRate = if (income.minorUnits == 0L) {
            0.0
        } else {
            balance.minorUnits.toDouble() / income.minorUnits.toDouble()
        }

        return MonthlySummary(
            month = month,
            income = income,
            expenses = expenses,
            balance = balance,
            savingsRate = savingsRate,
        )
    }
}
