package com.fintrack.domain.model

import java.time.Instant
import java.time.LocalDate

data class Transaction(
    val id: Long,
    val type: TransactionType,
    val amount: Money,
    val title: String,
    val categoryId: Long,
    val date: LocalDate,
    val notes: String?,
    val isRecurring: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
)
