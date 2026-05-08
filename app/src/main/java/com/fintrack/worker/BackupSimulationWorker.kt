package com.fintrack.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.time.Instant

class BackupSimulationWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : Worker(appContext, workerParams) {
    override fun doWork(): ListenableWorker.Result = runCatching {
        File(applicationContext.filesDir, "backup-simulation.txt")
            .writeText("Backup simulated at ${Instant.now()}")
        ListenableWorker.Result.success()
    }.getOrElse {
        ListenableWorker.Result.failure()
    }
}
