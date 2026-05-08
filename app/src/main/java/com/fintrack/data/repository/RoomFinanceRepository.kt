package com.fintrack.data.repository

import android.content.SharedPreferences
import com.fintrack.data.local.dao.BudgetDao
import com.fintrack.data.local.dao.CategoryDao
import com.fintrack.data.local.dao.TransactionDao
import com.fintrack.data.local.mapper.toDomain
import com.fintrack.data.local.mapper.toEntity
import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.Category
import com.fintrack.domain.model.MonthlySummary
import com.fintrack.domain.model.ReportData
import com.fintrack.domain.model.Settings
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.repository.FinanceRepository
import com.fintrack.domain.usecase.BuildReportDataUseCase
import com.fintrack.domain.usecase.CalculateMonthlySummaryUseCase
import com.fintrack.domain.usecase.GenerateCsvUseCase
import java.time.YearMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class RoomFinanceRepository(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao,
    private val preferences: SharedPreferences,
    private val calculateMonthlySummary: CalculateMonthlySummaryUseCase = CalculateMonthlySummaryUseCase(),
    private val buildReportData: BuildReportDataUseCase = BuildReportDataUseCase(),
    private val generateCsv: GenerateCsvUseCase = GenerateCsvUseCase(),
) : FinanceRepository {
    private val settings = MutableStateFlow(readSettings())

    override fun observeTransactions(): Flow<List<Transaction>> =
        transactionDao.observeTransactions().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getTransaction(id: Long): Transaction? =
        transactionDao.getTransaction(id)?.toDomain()

    override suspend fun upsertTransaction(transaction: Transaction): Long =
        transactionDao.upsert(transaction.toEntity())

    override suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteById(id)
    }

    override fun observeCategories(): Flow<List<Category>> =
        categoryDao.observeCategories().map { entities -> entities.map { it.toDomain() } }

    override suspend fun upsertCategory(category: Category): Long =
        categoryDao.upsert(category.toEntity())

    override suspend fun deleteCategory(id: Long) {
        categoryDao.deleteById(id)
    }

    override fun observeBudgets(): Flow<List<Budget>> =
        budgetDao.observeBudgets().map { entities -> entities.map { it.toDomain() } }

    override suspend fun upsertBudget(budget: Budget): Long =
        budgetDao.upsert(budget.toEntity())

    override suspend fun deleteBudget(id: Long) {
        budgetDao.deleteById(id)
    }

    override fun getDashboardSummary(month: YearMonth): Flow<MonthlySummary> =
        observeTransactions().map { transactions -> calculateMonthlySummary(transactions, month) }

    override fun getReportData(month: YearMonth): Flow<ReportData> =
        combine(observeTransactions(), observeCategories()) { transactions, categories ->
            buildReportData(transactions, categories, month)
        }

    override suspend fun exportTransactions(): String =
        generateCsv(
            transactions = transactionDao.getTransactionsSnapshot().map { it.toDomain() },
            categories = categoryDao.getCategoriesSnapshot().map { it.toDomain() },
        )

    override fun observeSettings(): Flow<Settings> = settings

    override suspend fun updateSettings(settings: Settings) {
        preferences.edit()
            .putString(KEY_CURRENCY_CODE, settings.currencyCode)
            .putBoolean(KEY_DARK_MODE, settings.darkModeEnabled)
            .apply()
        this.settings.value = settings
    }

    override suspend fun clearLocalData() {
        transactionDao.deleteAll()
        budgetDao.deleteAll()
        categoryDao.deleteAll()
    }

    private fun readSettings(): Settings = Settings(
        currencyCode = preferences.getString(KEY_CURRENCY_CODE, null) ?: "USD",
        darkModeEnabled = preferences.getBoolean(KEY_DARK_MODE, false),
    )

    private companion object {
        const val KEY_CURRENCY_CODE = "currency_code"
        const val KEY_DARK_MODE = "dark_mode"
    }
}
