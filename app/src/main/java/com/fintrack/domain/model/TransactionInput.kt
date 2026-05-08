package com.fintrack.domain.model

import java.time.LocalDate

data class TransactionInput(
    val id: Long?,
    val title: String,
    val amountText: String,
    val type: TransactionType,
    val categoryId: Long?,
    val date: LocalDate?,
    val notes: String?,
    val isRecurring: Boolean,
)

data class TransactionDraft(
    val id: Long?,
    val type: TransactionType,
    val amount: Money,
    val title: String,
    val categoryId: Long,
    val date: LocalDate,
    val notes: String?,
    val isRecurring: Boolean,
)

data class TransactionValidationResult(
    val isValid: Boolean,
    val fieldErrors: Map<String, String>,
    val draft: TransactionDraft?,
)
