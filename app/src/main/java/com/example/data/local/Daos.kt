package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    @Query("DELETE FROM projects")
    suspend fun clearAll()
}

@Dao
interface BrandKitDao {
    @Query("SELECT * FROM brand_kits ORDER BY id DESC")
    fun getAllBrandKits(): Flow<List<BrandKitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrandKit(brandKit: BrandKitEntity): Long

    @Query("DELETE FROM brand_kits WHERE id = :id")
    suspend fun deleteBrandKitById(id: Long)
}

@Dao
interface TemplateDao {
    @Query("SELECT * FROM templates ORDER BY category ASC")
    fun getAllTemplates(): Flow<List<TemplateEntity>>

    @Query("SELECT * FROM templates WHERE category = :category")
    fun getTemplatesByCategory(category: String): Flow<List<TemplateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplates(templates: List<TemplateEntity>)
}
