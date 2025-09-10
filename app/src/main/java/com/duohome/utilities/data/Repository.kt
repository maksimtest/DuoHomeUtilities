package com.duohome.utilities.data

import com.duohome.utilities.dao.*
import com.duohome.utilities.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val dao: HomeDao
) {
    fun observeHomes(): Flow<List<Home>> = dao.observeAll()
    suspend fun get(id: Long) = dao.getById(id)
    suspend fun upsert(id: Long?, name: String): Long {
        return if (id == null || id == 0L) dao.insert(Home(name = name))
        else { dao.update(Home(id = id, name = name)); id }
    }
}

class TemplateRepository @Inject constructor(
    private val dao: TemplateUtilityDao
) {
    fun observeTemplates(): Flow<List<TemplateUtility>> = dao.observeAll()
    suspend fun seedIfEmpty(defaults: List<TemplateUtility>) {
        // В простоте: вызывай при старте, Room сам защитится от дублей на пустой базе
        dao.insertAll(defaults)
    }
}

class UtilityRepository @Inject constructor(
    private val dao: UtilityDao
) {
    fun observeForHome(homeId: Long): Flow<List<UtilityWithTemplate>> = dao.observeForHome(homeId)
    suspend fun get(id: Long) = dao.getById(id)
    suspend fun upsert(id: Long?, name: String, homeId: Long, templateId: Long): Long {
        return if (id == null || id == 0L)
            dao.insert(Utility(name = name, homeId = homeId, templateId = templateId))
        else { dao.update(Utility(id = id, name = name, homeId = homeId, templateId = templateId)); id }
    }
}
