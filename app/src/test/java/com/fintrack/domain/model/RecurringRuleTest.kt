package com.fintrack.domain.model

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class RecurringRuleTest {
    @Test
    fun weeklyRuleAdvancesSevenDays() {
        val rule = RecurringRule(
            id = 1,
            transactionTemplateId = 10,
            frequency = RecurringFrequency.WEEKLY,
            nextRunAt = Instant.parse("2026-05-08T09:00:00Z"),
        )

        assertEquals(
            Instant.parse("2026-05-15T09:00:00Z"),
            rule.calculateNextRun(),
        )
    }

    @Test
    fun monthlyRuleAdvancesOneCalendarMonth() {
        val rule = RecurringRule(
            id = 1,
            transactionTemplateId = 10,
            frequency = RecurringFrequency.MONTHLY,
            nextRunAt = Instant.parse("2026-01-31T09:00:00Z"),
        )

        assertEquals(
            Instant.parse("2026-02-28T09:00:00Z"),
            rule.calculateNextRun(),
        )
    }
}
