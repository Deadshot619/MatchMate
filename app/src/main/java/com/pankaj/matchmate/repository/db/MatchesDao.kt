package com.pankaj.matchmate.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchesDao {
    @Query("SELECT * FROM matches")
    fun getAllMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :id")
    fun getParticularMatch(id: String): MatchEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(matches: List<MatchEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)
}