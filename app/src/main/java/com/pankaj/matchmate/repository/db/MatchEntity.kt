package com.pankaj.matchmate.repository.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pankaj.matchmate.network.models.MatchesModel

enum class MatchStatus { ACCEPTED, DECLINED, NONE }

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val location: String,
    val photoUrl: String,
    val status: MatchStatus = MatchStatus.NONE
)

fun MatchesModel.toMatchEntity(): MatchEntity = MatchEntity(
    id = login.uuid,
    firstName = name.first,
    lastName = name.last,
    age = dob.age,
    location = location.city,
    photoUrl = picture.large
)
