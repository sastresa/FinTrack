package com.fintrack.di

import android.content.Context
import androidx.room.Room
import com.fintrack.BuildConfig
import com.fintrack.data.local.db.FinTrackDatabase
import com.fintrack.data.repository.RoomFinanceRepository
import com.fintrack.domain.repository.FinanceRepository
import com.fintrack.domain.usecase.FinanceUseCases
import com.fintrack.worker.BackgroundWorkScheduler
import com.fintrack.worker.WorkManagerBackgroundWorkScheduler

class AppContainer(
    val context: Context,
) {
    private val database: FinTrackDatabase by lazy {
        val builder = Room.databaseBuilder(
            context.applicationContext,
            FinTrackDatabase::class.java,
            "fintrack.db",
        )
        if (BuildConfig.DEBUG) {
            builder.fallbackToDestructiveMigration(dropAllTables = true)
        }
        builder.build()
    }

    val repository: FinanceRepository by lazy {
        RoomFinanceRepository(
            transactionDao = database.transactionDao(),
            categoryDao = database.categoryDao(),
            budgetDao = database.budgetDao(),
            preferences = context.getSharedPreferences("fintrack_settings", Context.MODE_PRIVATE),
        )
    }

    val useCases: FinanceUseCases by lazy {
        FinanceUseCases.from(repository)
    }

    val backgroundWorkScheduler: BackgroundWorkScheduler by lazy {
        WorkManagerBackgroundWorkScheduler(context)
    }
}
