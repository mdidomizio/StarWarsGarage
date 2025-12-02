package com.example.starwarsgarage.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class StarshipListInfo(
    val count: Int?,
    val pages: Int?,
    val next: String?,
    val prev: String?
)

data class StarshipResponse(
    val info: StarshipListInfo,
    val data: List<StarshipBasic>
)

interface StarshipApi {
    @GET("api/v1/vehicles")
    suspend fun getStarships(@Query("page") page: Int): StarshipResponse

    @GET("api/v1/vehicles/{id}")
    suspend fun getStarshipById(@Path("id") id: String): StarshipBasic
}
