package com.fintrack.domain.model

import java.time.YearMonth

data class Budget(
    val id: Long,
    val categoryId: Long?,
    val month: YearMonth,
    val limit: Money,
)
