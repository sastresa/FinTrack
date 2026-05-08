package com.fintrack.domain.model

import java.time.YearMonth

data class BudgetInput(
    val id: Long?,
    val categoryId: Long?,
    val month: YearMonth?,
    val limitAmountText: String,
)

data class BudgetDraft(
    val id: Long?,
    val categoryId: Long?,
    val month: YearMonth,
    val limit: Money,
)

data class BudgetValidationResult(
    val isValid: Boolean,
    val fieldErrors: Map<String, String>,
    val draft: BudgetDraft?,
)
