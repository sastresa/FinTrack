package com.fintrack.domain.usecase

import com.fintrack.domain.model.BudgetDraft
import com.fintrack.domain.model.BudgetInput
import com.fintrack.domain.model.BudgetValidationResult

class ValidateBudgetInputUseCase(
    private val parseMoney: ParseMoneyUseCase = ParseMoneyUseCase(),
) {
    operator fun invoke(input: BudgetInput): BudgetValidationResult {
        val errors = linkedMapOf<String, String>()
        val limit = parseMoney(input.limitAmountText).getOrElse {
            errors["limit"] = it.message ?: "Budget limit must be greater than 0"
            null
        }
        val month = input.month
        if (month == null) {
            errors["month"] = "Month is required"
        }

        val draft = if (errors.isEmpty() && limit != null && month != null) {
            BudgetDraft(
                id = input.id,
                categoryId = input.categoryId,
                month = month,
                limit = limit,
            )
        } else {
            null
        }

        return BudgetValidationResult(
            isValid = errors.isEmpty(),
            fieldErrors = errors,
            draft = draft,
        )
    }
}
