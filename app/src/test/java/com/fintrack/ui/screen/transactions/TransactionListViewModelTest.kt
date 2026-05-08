package com.fintrack.ui.screen.transactions

import com.fintrack.MainDispatcherRule
import com.fintrack.TestFixtures
import com.fintrack.data.FakeFinanceRepository
import com.fintrack.domain.model.TransactionType
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun searchAndTypeFilterUpdateVisibleTransactions() = runTest {
        val repository = FakeFinanceRepository()
        repository.seedCategories(TestFixtures.groceries, TestFixtures.salary)
        repository.seedTransactions(
            TestFixtures.transaction(1, title = "Coffee", type = TransactionType.EXPENSE, amount = 450, date = LocalDate.of(2026, 5, 8)),
            TestFixtures.transaction(2, title = "Salary", type = TransactionType.INCOME, amount = 500000, categoryId = TestFixtures.salary.id, date = LocalDate.of(2026, 5, 1)),
            TestFixtures.transaction(3, title = "Coffee beans", type = TransactionType.EXPENSE, amount = 1450, date = LocalDate.of(2026, 4, 28)),
        )
        val viewModel = TransactionListViewModel(FinanceUseCases.from(repository))
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("coffee")
        viewModel.onTypeFilterChanged(TransactionType.EXPENSE)
        viewModel.onDateRangeChanged(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31))
        advanceUntilIdle()

        assertEquals(listOf("Coffee"), viewModel.uiState.value.transactions.map { it.title })
        assertEquals(listOf(LocalDate.of(2026, 5, 8)), viewModel.uiState.value.groupedTransactions.keys.toList())
    }
}
