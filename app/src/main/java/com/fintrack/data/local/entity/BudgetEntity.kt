package com.fintrack.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.YearMonth

@Entity(
    tableName = "budgets",
    indices = [Index("categoryId"), Index("month")],
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: Long?,
    val month: YearMonth,
    val limitAmountMinor: Long,
)
