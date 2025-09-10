package com.duohome.utilities.di


import android.content.Context
import androidx.room.Room
import com.duohome.utilities.data.*
import com.duohome.utilities.dao.*
import com.duohome.utilities.entity.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "duohome.db")
            .fallbackToDestructiveMigration(false) // MVP
            .build()

    @Provides fun homeDao(db: AppDatabase): HomeDao = db.homeDao()
    @Provides fun templateDao(db: AppDatabase): TemplateUtilityDao = db.templateDao()
    @Provides fun utilityDao(db: AppDatabase): UtilityDao = db.utilityDao()

    // Простейший сидинг шаблонов (вода/газ/электро/интернет) при первом запуске:
    @Provides @Singleton
    fun seedTemplatesOnce(repo: TemplateRepository): Any {
        CoroutineScope(Dispatchers.IO).launch {
            repo.seedIfEmpty(
                listOf(
                    TemplateUtility(name = "Water"),
                    TemplateUtility(name = "Electricity"),
                    TemplateUtility(name = "Gas"),
                    TemplateUtility(name = "Internet")
                )
            )
        }
        return Any()
    }
}
