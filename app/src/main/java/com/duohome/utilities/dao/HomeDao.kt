package com.duohome.utilities.dao

import androidx.room.*
import com.duohome.utilities.entity.Home
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Query("SELECT * FROM homes ORDER BY name")
    fun observeAll(): Flow<List<Home>>

    @Query("SELECT * FROM homes WHERE id=:id")
    suspend fun getById(id: Long): Home?

    @Insert
    suspend fun insert(home: Home): Long

    @Update
    suspend fun update(home: Home)

    @Delete
    suspend fun delete(home: Home)
}