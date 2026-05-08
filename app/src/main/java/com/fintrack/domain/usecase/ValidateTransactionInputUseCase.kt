package com.fintrack.domain.usecase

import com.fintrack.domain.model.TransactionDraft
import com.fintrack.domain.model.TransactionInput
import com.fintrack.domain.model.TransactionValidationResult

class ValidateTransactionInputUseCase(
    private val parseMoney: ParseMoneyUseCase = ParseMoneyUseCase(),
) {
    operator fun invoke(input: TransactionInput): TransactionValidationResult {
        val errors = linkedMapOf<String, String>()

        val title = input.title.trim()
        if (title.isEmpty()) {
            errors["title"] = "Title is required"
        }

        val amount = parseMoney(input.amountText).getOrElse {
            errors["amount"] = if (input.amountText.trim().isEmpty()) {
                "Amount is required"
            } else {
                it.message ?: "Amount must be greater than 0"
            }
            null
        }

        val categoryId = input.categoryId
        if (categoryId == null) {
            errors["category"] = "Category is required"
        }

        val date = input.date
        if (date == null) {
            errors["date"] = "Date is required"
        }

        val draft = if (errors.isEmpty() && amount != null && categoryId != null && date != null) {
            TransactionDraft(
                id = input.id,
                type = input.type,
                amount = amount,
                title = title,
                categoryId = categoryId,
                date = date,
                notes = input.notes?.trim()?.ifEmpty { null },
                isRecurring = input.isRecurring,
            )
        } else {
            null
        }

        return TransactionValidationResult(
            isValid = errors.isEmpty(),
            fieldErrors = errors,
            draft = draft,
        )
    }
}
