package com.fintrack.data.local.db

import android.content.Context
import androidx.room.Room

object FinTrackDatabaseFactory {
    const val DATABASE_NAME = "fintrack.db"

    fun create(
        context: Context,
        databaseName: String = DATABASE_NAME,
    ): FinTrackDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            FinTrackDatabase::class.java,
            databaseName,
        ).build()
}
