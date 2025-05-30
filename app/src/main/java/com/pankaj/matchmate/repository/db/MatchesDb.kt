package com.pankaj.matchmate.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MatchEntity::class], version = 1, exportSchema = false)
abstract class MatchesDb: RoomDatabase() {
    abstract fun matchDao(): MatchesDao
}