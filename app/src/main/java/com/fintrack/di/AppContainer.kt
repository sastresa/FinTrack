package com.fintrack.di

import android.content.Context
import com.fintrack.data.bootstrap.DefaultCategorySeeder
import com.fintrack.data.local.db.FinTrackDatabase
import com.fintrack.data.local.db.FinTrackDatabaseFactory
import com.fintrack.data.repository.RoomFinanceRepository
import com.fintrack.domain.repository.FinanceRepository
import com.fintrack.domain.usecase.FinanceUseCases
import com.fintrack.worker.BackgroundWorkScheduler
import com.fintrack.worker.WorkManagerBackgroundWorkScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppContainer(
    val context: Context,
) {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database: FinTrackDatabase by lazy {
        FinTrackDatabaseFactory.create(context)
    }

    val repository: FinanceRepository by lazy {
        RoomFinanceRepository(
            transactionDao = database.transactionDao(),
            categoryDao = database.categoryDao(),
            budgetDao = database.budgetDao(),
            preferences = context.getSharedPreferences("fintrack_settings", Context.MODE_PRIVATE),
        ).also { repository ->
            applicationScope.launch {
                DefaultCategorySeeder(repository).ensureSeeded()
            }
        }
    }

    val useCases: FinanceUseCases by lazy {
        FinanceUseCases.from(repository)
    }

    val backgroundWorkScheduler: BackgroundWorkScheduler by lazy {
        WorkManagerBackgroundWorkScheduler(context)
    }
}
