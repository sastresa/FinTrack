package com.fintrack.data.local.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FinTrackDatabaseSafetyTest {
    @Test
    fun databaseFactoryDoesNotDestructivelyRecreateExistingDatabaseOnSchemaMismatch() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val databaseName = "fintrack-safety-${System.nanoTime()}.db"
        val databasePath = context.getDatabasePath(databaseName)
        context.deleteDatabase(databaseName)
        createLegacyDatabase(databasePath)

        try {
            assertThrows(IllegalStateException::class.java) {
                FinTrackDatabaseFactory.create(context, databaseName).openHelper.writableDatabase
            }
            assertEquals(1, legacyRowCount(databasePath))
        } finally {
            context.deleteDatabase(databaseName)
        }
    }

    private fun createLegacyDatabase(path: File) {
        path.parentFile?.mkdirs()
        val database = SQLiteDatabase.openOrCreateDatabase(path, null)
        try {
            database.execSQL("CREATE TABLE legacy_data (id INTEGER PRIMARY KEY, value TEXT NOT NULL)")
            database.execSQL("INSERT INTO legacy_data (id, value) VALUES (1, 'keep')")
            database.version = 1
        } finally {
            database.close()
        }
    }

    private fun legacyRowCount(path: File): Int {
        val database = SQLiteDatabase.openDatabase(path.path, null, SQLiteDatabase.OPEN_READONLY)
        return try {
            database.rawQuery("SELECT COUNT(*) FROM legacy_data", emptyArray()).use { cursor ->
                cursor.moveToFirst()
                cursor.getInt(0)
            }
        } finally {
            database.close()
        }
    }
}
