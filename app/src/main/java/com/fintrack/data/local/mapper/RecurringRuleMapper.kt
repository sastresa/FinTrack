package com.fintrack.data.local.mapper

import com.fintrack.data.local.entity.RecurringRuleEntity
import com.fintrack.domain.model.RecurringFrequency
import com.fintrack.domain.model.RecurringRule

fun RecurringRuleEntity.toDomain(): RecurringRule = RecurringRule(
    id = id,
    transactionTemplateId = transactionTemplateId,
    frequency = when (frequency) {
        "WEEKLY" -> RecurringFrequency.WEEKLY
        "MONTHLY" -> RecurringFrequency.MONTHLY
        "CUSTOM" -> RecurringFrequency.CUSTOM(requireNotNull(customIntervalDays))
        else -> error("Unknown recurring frequency: $frequency")
    },
    nextRunAt = nextRunAt,
)

fun RecurringRule.toEntity(): RecurringRuleEntity {
    val custom = frequency as? RecurringFrequency.CUSTOM
    return RecurringRuleEntity(
        id = id,
        transactionTemplateId = transactionTemplateId,
        frequency = when (frequency) {
            RecurringFrequency.WEEKLY -> "WEEKLY"
            RecurringFrequency.MONTHLY -> "MONTHLY"
            is RecurringFrequency.CUSTOM -> "CUSTOM"
        },
        customIntervalDays = custom?.intervalDays,
        nextRunAt = nextRunAt,
    )
}
