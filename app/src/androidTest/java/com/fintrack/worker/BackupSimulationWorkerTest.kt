package com.fintrack.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupSimulationWorkerTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun backupWorkerWritesSimulationMarker() {
        val worker = TestListenableWorkerBuilder<BackupSimulationWorker>(context).build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success()::class, result::class)
        assertTrue(context.filesDir.resolve("backup-simulation.txt").exists())
    }
}
