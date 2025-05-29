package com.pankaj.matchmate.network

import com.pankaj.matchmate.network.models.MatchesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchApi {

    @GET("/api")
    suspend fun getMatches(
        @Query ("results") result: Int,
    ): MatchesResponse
}