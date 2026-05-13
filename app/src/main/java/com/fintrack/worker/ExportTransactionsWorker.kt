package com.fintrack.worker

import android.content.Context
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fintrack.data.local.db.FinTrackDatabaseFactory
import com.fintrack.data.repository.RoomFinanceRepository
import java.io.File
import kotlinx.coroutines.runBlocking

class ExportTransactionsWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : Worker(appContext, workerParams) {
    override fun doWork(): ListenableWorker.Result {
        val fileName = inputData.getString(KEY_FILE_NAME) ?: defaultFileName()
        if (!isSafeCsvFileName(fileName)) {
            return ListenableWorker.Result.failure()
        }

        return runCatching {
            val csv = inputData.getString(KEY_CSV_CONTENT) ?: loadCsvFromRepository()
            val exportDir = File(applicationContext.filesDir, "exports").apply { mkdirs() }
            val exportFile = File(exportDir, fileName)
            exportFile.writeText(csv)
            ListenableWorker.Result.success(
                Data.Builder()
                    .putString(KEY_OUTPUT_PATH, exportFile.absolutePath)
                    .build(),
            )
        }.getOrElse {
            ListenableWorker.Result.failure()
        }
    }

    private fun loadCsvFromRepository(): String {
        val database = FinTrackDatabaseFactory.create(applicationContext)
        return try {
            val repository = RoomFinanceRepository(
                transactionDao = database.transactionDao(),
                categoryDao = database.categoryDao(),
                budgetDao = database.budgetDao(),
                preferences = applicationContext.getSharedPreferences("fintrack_settings", Context.MODE_PRIVATE),
            )
            runBlocking { repository.exportTransactions() }
        } finally {
            database.close()
        }
    }

    private fun defaultFileName(): String = "fintrack-export-${System.currentTimeMillis()}.csv"

    private fun isSafeCsvFileName(fileName: String): Boolean {
        val asFile = File(fileName)
        return fileName.endsWith(".csv") &&
            asFile.name == fileName &&
            !fileName.contains("..") &&
            fileName.isNotBlank()
    }

    companion object {
        const val KEY_FILE_NAME = "file_name"
        const val KEY_CSV_CONTENT = "csv_content"
        const val KEY_OUTPUT_PATH = "output_path"

        fun inputData(fileName: String, csvContent: String): Data =
            Data.Builder()
                .putString(KEY_FILE_NAME, fileName)
                .putString(KEY_CSV_CONTENT, csvContent)
                .build()
    }
}
