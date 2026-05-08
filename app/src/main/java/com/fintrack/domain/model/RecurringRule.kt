package com.fintrack.domain.model

import java.time.Instant
import java.time.ZoneOffset

data class RecurringRule(
    val id: Long,
    val transactionTemplateId: Long,
    val frequency: RecurringFrequency,
    val nextRunAt: Instant,
) {
    fun calculateNextRun(): Instant {
        val zoned = nextRunAt.atZone(ZoneOffset.UTC)
        return when (frequency) {
            RecurringFrequency.WEEKLY -> zoned.plusWeeks(1)
            RecurringFrequency.MONTHLY -> zoned.plusMonths(1)
            is RecurringFrequency.CUSTOM -> zoned.plusDays(frequency.intervalDays.toLong())
        }.toInstant()
    }
}

sealed interface RecurringFrequency {
    data object WEEKLY : RecurringFrequency
    data object MONTHLY : RecurringFrequency
    data class CUSTOM(val intervalDays: Int) : RecurringFrequency {
        init {
            require(intervalDays > 0) { "Custom interval must be positive" }
        }
    }
}
