package com.fintrack.data

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
import kotlinx.coroutines.flow.update

class FakeFinanceRepository : FinanceRepository {
    private val transactions = MutableStateFlow<List<Transaction>>(emptyList())
    private val categories = MutableStateFlow<List<Category>>(emptyList())
    private val budgets = MutableStateFlow<List<Budget>>(emptyList())
    private val settings = MutableStateFlow(Settings())
    private val calculateMonthlySummary = CalculateMonthlySummaryUseCase()
    private val buildReportData = BuildReportDataUseCase()
    private val generateCsv = GenerateCsvUseCase()

    fun seedTransactions(vararg values: Transaction) {
        transactions.value = values.toList()
    }

    fun seedCategories(vararg values: Category) {
        categories.value = values.toList()
    }

    fun seedBudgets(vararg values: Budget) {
        budgets.value = values.toList()
    }

    fun transactionsSnapshot(): List<Transaction> = transactions.value

    override fun observeTransactions(): Flow<List<Transaction>> = transactions

    override suspend fun getTransaction(id: Long): Transaction? = transactions.value.firstOrNull { it.id == id }

    override suspend fun upsertTransaction(transaction: Transaction): Long {
        val id = if (transaction.id == 0L) nextId(transactions.value.map { it.id }) else transaction.id
        val saved = transaction.copy(id = id)
        transactions.update { current -> current.filterNot { it.id == id } + saved }
        return id
    }

    override suspend fun deleteTransaction(id: Long) {
        transactions.update { current -> current.filterNot { it.id == id } }
    }

    override fun observeCategories(): Flow<List<Category>> = categories

    override suspend fun upsertCategory(category: Category): Long {
        val id = if (category.id == 0L) nextId(categories.value.map { it.id }) else category.id
        categories.update { current -> current.filterNot { it.id == id } + category.copy(id = id) }
        return id
    }

    override suspend fun deleteCategory(id: Long) {
        categories.update { current -> current.filterNot { it.id == id } }
    }

    override fun observeBudgets(): Flow<List<Budget>> = budgets

    override suspend fun upsertBudget(budget: Budget): Long {
        val id = if (budget.id == 0L) nextId(budgets.value.map { it.id }) else budget.id
        budgets.update { current -> current.filterNot { it.id == id } + budget.copy(id = id) }
        return id
    }

    override suspend fun deleteBudget(id: Long) {
        budgets.update { current -> current.filterNot { it.id == id } }
    }

    override fun getDashboardSummary(month: YearMonth): Flow<MonthlySummary> =
        transactions.combine(categories) { values, _ -> calculateMonthlySummary(values, month) }

    override fun getReportData(month: YearMonth): Flow<ReportData> =
        transactions.combine(categories) { transactionValues, categoryValues ->
            buildReportData(transactionValues, categoryValues, month)
        }

    override suspend fun exportTransactions(): String = generateCsv(transactions.value, categories.value)

    override fun observeSettings(): Flow<Settings> = settings

    override suspend fun updateSettings(settings: Settings) {
        this.settings.value = settings
    }

    override suspend fun clearLocalData() {
        transactions.value = emptyList()
        categories.value = emptyList()
        budgets.value = emptyList()
    }

    private fun nextId(ids: List<Long>): Long = (ids.maxOrNull() ?: 0L) + 1L
}
