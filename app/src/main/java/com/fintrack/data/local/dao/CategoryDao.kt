package com.fintrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fintrack.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name COLLATE NOCASE ASC")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories ORDER BY name COLLATE NOCASE ASC")
    suspend fun getCategoriesSnapshot(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(category: CategoryEntity): Long

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM categories")
    suspend fun deleteAll()
}
