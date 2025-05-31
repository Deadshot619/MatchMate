package com.pankaj.matchmate.repository

import com.pankaj.matchmate.network.MatchApi
import com.pankaj.matchmate.repository.db.MatchEntity
import com.pankaj.matchmate.repository.db.MatchStatus
import com.pankaj.matchmate.repository.db.MatchesDao
import com.pankaj.matchmate.repository.db.toMatchEntity
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MatchesRepository @Inject constructor(
    private val api: MatchApi,
    private val matchesDao: MatchesDao
) {
    val matchesList = matchesDao.getAllMatches()

    fun fetchAndSaveMatches(numItems: Int) = flow<Result<Boolean>> {
        emit(Result.Loading())

        try {
            val response = api.getMatches(numItems)
            val matches = response.results.map { it.toMatchEntity() }
            matchesDao.insertAll(matches)
            emit(Result.Success(data = true))
        } catch (e: Exception) {
            emit(Result.Error(data = false, message = e.message ?: "Unknown error"))
        }
    }

    suspend fun updateMatchStatus(id: String, status: MatchStatus) {
        val currMatch = matchesDao.getParticularMatch(id)
        matchesDao.updateMatch(currMatch.copy(status = status))
    }
}