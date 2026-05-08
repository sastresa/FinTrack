package com.fintrack.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "recurring_rules",
    indices = [Index("transactionTemplateId")],
)
data class RecurringRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transactionTemplateId: Long,
    val frequency: String,
    val customIntervalDays: Int?,
    val nextRunAt: Instant,
)
