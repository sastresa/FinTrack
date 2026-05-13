package com.fintrack.data.bootstrap

import com.fintrack.data.FakeFinanceRepository
import com.fintrack.domain.model.CategoryType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultCategorySeederTest {
    @Test
    fun seederAddsIncomeAndExpenseCategoriesWhenNoneExist() = runTest {
        val repository = FakeFinanceRepository()

        DefaultCategorySeeder(repository).ensureSeeded()

        val categories = repository.observeCategories().first()
        assertTrue(categories.any { it.type == CategoryType.EXPENSE })
        assertTrue(categories.any { it.type == CategoryType.INCOME })
        assertTrue(categories.any { it.type == CategoryType.BOTH })
    }

    @Test
    fun seederLeavesExistingCategoriesAlone() = runTest {
        val repository = FakeFinanceRepository()
        repository.seedCategories(DefaultCategories.values.first())

        DefaultCategorySeeder(repository).ensureSeeded()

        assertTrue(repository.categoriesSnapshot().size == 1)
    }
}
