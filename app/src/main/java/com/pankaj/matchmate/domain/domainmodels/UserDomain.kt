package com.pankaj.matchmate.domain.domainmodels

import com.pankaj.matchmate.repository.db.MatchEntity
import com.pankaj.matchmate.repository.db.MatchStatus

data class UserDomain(
    val id: String,
    val name: String,
    val age: Int,
    val location: String,
    val photoUrl: String,
    val matchStatus: MatchStatus,
)

fun MatchEntity.toUserDomain() = UserDomain(
    id = id,
    name = "$firstName $lastName",
    age = age,
    location = location,
    photoUrl = photoUrl,
    matchStatus = status
)
