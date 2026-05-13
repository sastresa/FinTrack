package com.fintrack.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.fintrack.TestFixtures
import com.fintrack.data.local.db.FinTrackDatabaseFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RoomFinanceRepositoryPersistenceTest {
    @Test
    fun transactionsPersistAfterDatabaseIsClosedAndReopened() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val databaseName = "fintrack-persistence-${System.nanoTime()}.db"
        context.deleteDatabase(databaseName)
        val preferences = context.getSharedPreferences("fintrack-persistence-$databaseName", Context.MODE_PRIVATE)

        try {
            val writeDatabase = FinTrackDatabaseFactory.create(context, databaseName)
            try {
                val repository = RoomFinanceRepository(
                    transactionDao = writeDatabase.transactionDao(),
                    categoryDao = writeDatabase.categoryDao(),
                    budgetDao = writeDatabase.budgetDao(),
                    preferences = preferences,
                )
                repository.upsertCategory(TestFixtures.groceries)
                repository.upsertTransaction(TestFixtures.transaction(id = 0, title = "Persisted transaction"))
            } finally {
                writeDatabase.close()
            }

            val readDatabase = FinTrackDatabaseFactory.create(context, databaseName)
            try {
                val repository = RoomFinanceRepository(
                    transactionDao = readDatabase.transactionDao(),
                    categoryDao = readDatabase.categoryDao(),
                    budgetDao = readDatabase.budgetDao(),
                    preferences = preferences,
                )

                val saved = repository.observeTransactions().first().single()
                assertEquals("Persisted transaction", saved.title)
                assertEquals(TestFixtures.groceries.id, saved.categoryId)
            } finally {
                readDatabase.close()
            }
        } finally {
            preferences.edit().clear().commit()
            context.deleteDatabase(databaseName)
        }
    }
}
