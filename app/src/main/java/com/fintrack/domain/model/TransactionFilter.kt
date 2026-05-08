package com.fintrack.domain.model

import java.time.LocalDate

data class TransactionFilter(
    val query: String = "",
    val type: TransactionType? = null,
    val categoryIds: Set<Long> = emptySet(),
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
)
