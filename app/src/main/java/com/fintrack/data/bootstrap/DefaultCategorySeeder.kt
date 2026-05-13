package com.fintrack.data.bootstrap

import com.fintrack.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.first

class DefaultCategorySeeder(
    private val repository: FinanceRepository,
) {
    suspend fun ensureSeeded() {
        if (repository.observeCategories().first().isNotEmpty()) {
            return
        }

        DefaultCategories.values.forEach { category ->
            repository.upsertCategory(category)
        }
    }
}
