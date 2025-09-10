package com.duohome.utilities.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duohome.utilities.entity.*
import com.duohome.utilities.dao.*
@Database(
    entities = [Home::class, TemplateUtility::class, Utility::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeDao(): HomeDao
    abstract fun templateDao(): TemplateUtilityDao
    abstract fun utilityDao(): UtilityDao
}