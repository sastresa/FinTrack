package com.fintrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fintrack.data.local.entity.RecurringRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringRuleDao {
    @Query("SELECT * FROM recurring_rules ORDER BY nextRunAt ASC")
    fun observeRules(): Flow<List<RecurringRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rule: RecurringRuleEntity): Long

    @Query("DELETE FROM recurring_rules WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM recurring_rules")
    suspend fun deleteAll()
}
