package com.fintrack.ui.screen.dashboard

import com.fintrack.MainDispatcherRule
import com.fintrack.TestFixtures
import com.fintrack.data.FakeFinanceRepository
import com.fintrack.domain.model.Money
import com.fintrack.domain.model.TransactionType
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun dashboardUpdatesSummaryFromRepositoryFlows() = runTest {
        val repository = FakeFinanceRepository()
        repository.seedCategories(TestFixtures.groceries, TestFixtures.salary)
        repository.seedTransactions(
            TestFixtures.transaction(1, title = "May salary", type = TransactionType.INCOME, amount = 500000, categoryId = TestFixtures.salary.id, date = java.time.LocalDate.of(2026, 5, 1)),
            TestFixtures.transaction(2, title = "Groceries", type = TransactionType.EXPENSE, amount = 12500),
        )
        val viewModel = DashboardViewModel(
            useCases = FinanceUseCases.from(repository),
            clock = Clock.fixed(Instant.parse("2026-05-08T12:00:00Z"), ZoneOffset.UTC),
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(Money(500000), state.summary.income)
        assertEquals(Money(12500), state.summary.expenses)
        assertEquals(Money(487500), state.summary.balance)
        assertEquals(listOf("Groceries"), state.categoryBreakdown.map { it.categoryName })
        assertEquals(listOf("Groceries", "May salary"), state.recentTransactions.map { it.title })
    }
}
