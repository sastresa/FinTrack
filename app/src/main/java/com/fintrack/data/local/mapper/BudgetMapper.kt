package com.fintrack.data.local.mapper

import com.fintrack.data.local.entity.BudgetEntity
import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.Money

fun BudgetEntity.toDomain(): Budget = Budget(
    id = id,
    categoryId = categoryId,
    month = month,
    limit = Money(limitAmountMinor),
)

fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    categoryId = categoryId,
    month = month,
    limitAmountMinor = limit.minorUnits,
)
