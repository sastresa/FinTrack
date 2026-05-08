package com.fintrack.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

@Entity(
    tableName = "transactions",
    indices = [Index("categoryId"), Index("date")],
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val amountMinor: Long,
    val title: String,
    val categoryId: Long,
    val date: LocalDate,
    val notes: String?,
    val isRecurring: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
)
