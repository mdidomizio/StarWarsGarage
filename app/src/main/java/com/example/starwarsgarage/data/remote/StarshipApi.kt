package com.example.starwarsgarage.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

data class StarshipResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Starship>
)

interface StarshipApi {
    @GET("starships")
    suspend fun getStarships(): StarshipResponse

    @GET("starships/{id}")
    suspend fun getStarshipById(@Path("id") id: String): Starship
}
