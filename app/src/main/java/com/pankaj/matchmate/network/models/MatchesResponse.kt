package com.pankaj.matchmate.network.models

import com.pankaj.matchmate.repository.db.MatchEntity

data class MatchesResponse(
    val results: List<MatchesModel>
)

data class MatchesModel(
    val name: MatchName,
    val login: MatchLogin,
    val dob: MatchDob,
    val location: MatchLocation,
    val picture: MatchPicture
)

data class MatchLogin(val uuid: String)
data class MatchName(val first: String, val last: String)
data class MatchDob(val age: Int)
data class MatchLocation(val city: String)
data class MatchPicture(val large: String)
