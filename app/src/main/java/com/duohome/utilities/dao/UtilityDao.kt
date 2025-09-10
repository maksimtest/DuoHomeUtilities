package com.duohome.utilities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.duohome.utilities.entity.Utility
import com.duohome.utilities.entity.UtilityWithTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface UtilityDao {
    @Transaction
    @Query("SELECT * FROM utilities WHERE homeId=:homeId ORDER BY name")
    fun observeForHome(homeId: Long): Flow<List<UtilityWithTemplate>>

    @Query("SELECT * FROM utilities WHERE id=:id")
    suspend fun getById(id: Long): Utility?

    @Insert
    suspend fun insert(utility: Utility): Long

    @Update
    suspend fun update(utility: Utility)

    @Delete
    suspend fun delete(utility: Utility)
}