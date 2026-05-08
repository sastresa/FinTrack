package com.fintrack.domain.usecase

import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.BudgetProgress
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionType
import java.time.YearMonth

class CalculateBudgetProgressUseCase {
    operator fun invoke(
        budgets: List<Budget>,
        transactions: List<Transaction>,
    ): List<BudgetProgress> = budgets.map { budget ->
        val spent = transactions
            .asSequence()
            .filter { it.type == TransactionType.EXPENSE }
            .filter { YearMonth.from(it.date) == budget.month }
            .filter { budget.categoryId == null || it.categoryId == budget.categoryId }
            .fold(Money.ZERO) { total, transaction -> total + transaction.amount }
        val remaining = budget.limit - spent
        val percentage = if (budget.limit.minorUnits == 0L) {
            0.0
        } else {
            spent.minorUnits.toDouble() / budget.limit.minorUnits.toDouble()
        }

        BudgetProgress(
            budget = budget,
            spent = spent,
            remaining = remaining,
            percentage = percentage,
            isExceeded = spent > budget.limit,
        )
    }
}
