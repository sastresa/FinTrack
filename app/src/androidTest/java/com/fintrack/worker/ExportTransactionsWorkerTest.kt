package com.fintrack.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExportTransactionsWorkerTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun exportWorkerWritesCsvInputToLocalFile() {
        val worker = TestListenableWorkerBuilder<ExportTransactionsWorker>(context)
            .setInputData(ExportTransactionsWorker.inputData("transactions.csv", "id,title\n1,Coffee"))
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success()::class, result::class)
        val exportFile = context.filesDir.resolve("exports/transactions.csv")
        assertTrue(exportFile.exists())
        assertEquals("id,title\n1,Coffee", exportFile.readText())
    }

    @Test
    fun exportWorkerFailsForUnsafeFileName() {
        val worker = TestListenableWorkerBuilder<ExportTransactionsWorker>(context)
            .setInputData(ExportTransactionsWorker.inputData("../bad.csv", "id,title"))
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.failure()::class, result::class)
    }
}
