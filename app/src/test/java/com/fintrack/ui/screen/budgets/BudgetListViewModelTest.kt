package com.fintrack.ui.screen.budgets

import com.fintrack.MainDispatcherRule
import com.fintrack.TestFixtures
import com.fintrack.data.FakeFinanceRepository
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.YearMonth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun budgetProgressMarksExceededBudget() = runTest {
        val repository = FakeFinanceRepository()
        repository.seedBudgets(TestFixtures.budget(limit = 1000))
        repository.seedTransactions(TestFixtures.transaction(1, amount = 1500))
        val viewModel = BudgetListViewModel(FinanceUseCases.from(repository))

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.progress.single().isExceeded)
    }

    @Test
    fun saveValidBudgetWritesToRepositoryAndResetsForm() = runTest {
        val repository = FakeFinanceRepository()
        repository.seedCategories(TestFixtures.groceries)
        val viewModel = BudgetListViewModel(FinanceUseCases.from(repository))
        advanceUntilIdle()

        viewModel.onCategorySelected(TestFixtures.groceries.id)
        viewModel.onMonthChanged("2026-05")
        viewModel.onLimitAmountChanged("250.00")
        viewModel.onSaveClicked()
        advanceUntilIdle()

        val saved = repository.budgetsSnapshot().single()
        assertEquals(TestFixtures.groceries.id, saved.categoryId)
        assertEquals(YearMonth.of(2026, 5), saved.month)
        assertEquals(25000, saved.limit.minorUnits)
        assertEquals("", viewModel.uiState.value.limitAmountText)
    }

    @Test
    fun saveInvalidBudgetShowsValidationErrors() = runTest {
        val repository = FakeFinanceRepository()
        val viewModel = BudgetListViewModel(FinanceUseCases.from(repository))

        viewModel.onLimitAmountChanged("0")
        viewModel.onMonthChanged("")
        viewModel.onSaveClicked()
        advanceUntilIdle()

        assertEquals("Amount must be greater than 0", viewModel.uiState.value.fieldErrors["limit"])
        assertEquals("Month is required", viewModel.uiState.value.fieldErrors["month"])
        assertTrue(repository.budgetsSnapshot().isEmpty())
    }
}
