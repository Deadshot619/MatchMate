package com.pankaj.matchmate.repository

import com.pankaj.matchmate.network.MatchApi
import com.pankaj.matchmate.repository.db.MatchEntity
import com.pankaj.matchmate.repository.db.MatchesDao
import com.pankaj.matchmate.repository.db.toMatchEntity
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MatchesRepository @Inject constructor(
    private val api: MatchApi,
    private val matchesDao: MatchesDao
) {
    suspend fun getMatches() = flow<Result<List<MatchEntity>>> {
        emit(Result.Loading())

        try {
            val response = api.getMatches(10)
            val matches = response.results.map { it.toMatchEntity() }
            matchesDao.insertAll(matches)
            matchesDao.getAllMatches().collect {
                emit(Result.Success(it))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }
}