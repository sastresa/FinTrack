package com.fintrack.domain.usecase

import com.fintrack.domain.model.Budget
import com.fintrack.domain.model.Category
import com.fintrack.domain.model.ReportData
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.repository.FinanceRepository
import java.time.YearMonth
import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCase(private val repository: FinanceRepository) {
    operator fun invoke(): Flow<List<Transaction>> = repository.observeTransactions()
}

class GetTransactionUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(id: Long): Transaction? = repository.getTransaction(id)
}

class AddTransactionUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(transaction: Transaction): Long = repository.upsertTransaction(transaction)
}

class UpdateTransactionUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(transaction: Transaction): Long = repository.upsertTransaction(transaction)
}

class DeleteTransactionUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteTransaction(id)
}

class GetCategoriesUseCase(private val repository: FinanceRepository) {
    operator fun invoke(): Flow<List<Category>> = repository.observeCategories()
}

class AddCategoryUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(category: Category): Long = repository.upsertCategory(category)
}

class UpdateCategoryUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(category: Category): Long = repository.upsertCategory(category)
}

class DeleteCategoryUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteCategory(id)
}

class GetBudgetsUseCase(private val repository: FinanceRepository) {
    operator fun invoke(): Flow<List<Budget>> = repository.observeBudgets()
}

class AddBudgetUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(budget: Budget): Long = repository.upsertBudget(budget)
}

class UpdateBudgetUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(budget: Budget): Long = repository.upsertBudget(budget)
}

class DeleteBudgetUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteBudget(id)
}

class GetReportDataUseCase(private val repository: FinanceRepository) {
    operator fun invoke(month: YearMonth): Flow<ReportData> = repository.getReportData(month)
}

class ExportTransactionsUseCase(private val repository: FinanceRepository) {
    suspend operator fun invoke(): String = repository.exportTransactions()
}
