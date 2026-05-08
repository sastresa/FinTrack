package com.fintrack.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fintrack.data.local.dao.BudgetDao
import com.fintrack.data.local.dao.CategoryDao
import com.fintrack.data.local.dao.RecurringRuleDao
import com.fintrack.data.local.dao.TransactionDao
import com.fintrack.data.local.entity.BudgetEntity
import com.fintrack.data.local.entity.CategoryEntity
import com.fintrack.data.local.entity.RecurringRuleEntity
import com.fintrack.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class,
        RecurringRuleEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class FinTrackDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun recurringRuleDao(): RecurringRuleDao
}
