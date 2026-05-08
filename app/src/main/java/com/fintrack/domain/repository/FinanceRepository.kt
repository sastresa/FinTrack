package com.fintrack.domain.repository

import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.Category
import com.fintrack.domain.model.MonthlySummary
import com.fintrack.domain.model.ReportData
import com.fintrack.domain.model.Settings
import com.fintrack.domain.model.Transaction
import java.time.YearMonth
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    fun observeTransactions(): Flow<List<Transaction>>
    suspend fun getTransaction(id: Long): Transaction?
    suspend fun upsertTransaction(transaction: Transaction): Long
    suspend fun deleteTransaction(id: Long)

    fun observeCategories(): Flow<List<Category>>
    suspend fun upsertCategory(category: Category): Long
    suspend fun deleteCategory(id: Long)

    fun observeBudgets(): Flow<List<Budget>>
    suspend fun upsertBudget(budget: Budget): Long
    suspend fun deleteBudget(id: Long)

    fun getDashboardSummary(month: YearMonth): Flow<MonthlySummary>
    fun getReportData(month: YearMonth): Flow<ReportData>
    suspend fun exportTransactions(): String

    fun observeSettings(): Flow<Settings>
    suspend fun updateSettings(settings: Settings)
    suspend fun clearLocalData()
}
