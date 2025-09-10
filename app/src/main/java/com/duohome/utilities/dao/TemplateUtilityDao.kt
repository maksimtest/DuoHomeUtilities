package com.duohome.utilities.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.duohome.utilities.entity.TemplateUtility
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateUtilityDao {
    @Query("SELECT * FROM template_utilities ORDER BY name")
    fun observeAll(): Flow<List<TemplateUtility>>

    @Insert
    suspend fun insertAll(items: List<TemplateUtility>)
}