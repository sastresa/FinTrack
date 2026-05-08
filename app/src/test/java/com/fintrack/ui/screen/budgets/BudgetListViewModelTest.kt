package com.fintrack.ui.screen.budgets

import com.fintrack.MainDispatcherRule
import com.fintrack.TestFixtures
import com.fintrack.data.FakeFinanceRepository
import com.fintrack.domain.usecase.FinanceUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
}
