package com.fintrack.worker

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

interface BackgroundWorkScheduler {
    fun enqueueExport(csv: String)
    fun enqueueBackup()
}

object NoOpBackgroundWorkScheduler : BackgroundWorkScheduler {
    override fun enqueueExport(csv: String) = Unit
    override fun enqueueBackup() = Unit
}

class WorkManagerBackgroundWorkScheduler(
    context: Context,
) : BackgroundWorkScheduler {
    private val appContext = context.applicationContext

    override fun enqueueExport(csv: String) {
        val request = OneTimeWorkRequest.Builder(ExportTransactionsWorker::class.java)
            .setInputData(
                ExportTransactionsWorker.inputData(
                    fileName = "fintrack-export-${System.currentTimeMillis()}.csv",
                    csvContent = csv,
                ),
            )
            .build()
        WorkManager.getInstance(appContext).enqueue(request)
    }

    override fun enqueueBackup() {
        val request = OneTimeWorkRequest.Builder(BackupSimulationWorker::class.java).build()
        WorkManager.getInstance(appContext).enqueue(request)
    }
}
