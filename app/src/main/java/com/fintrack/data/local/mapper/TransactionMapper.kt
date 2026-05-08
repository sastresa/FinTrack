package com.fintrack.data.local.mapper

import com.fintrack.data.local.entity.TransactionEntity
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionType

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    type = TransactionType.valueOf(type),
    amount = Money(amountMinor),
    title = title,
    categoryId = categoryId,
    date = date,
    notes = notes,
    isRecurring = isRecurring,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    type = type.name,
    amountMinor = amount.minorUnits,
    title = title,
    categoryId = categoryId,
    date = date,
    notes = notes,
    isRecurring = isRecurring,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
