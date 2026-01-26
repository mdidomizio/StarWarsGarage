package com.example.starwarsgarage.data.remote.api

import com.example.starwarsgarage.data.remote.StarshipBasic
import com.example.starwarsgarage.data.remote.StarshipResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StarshipApi {
    @GET("api/v1/vehicles")
    suspend fun getStarships(
        @Query("page") page: Int,
        @Query("limit") limit:Int = 10
    ): StarshipResponse

    @GET("api/v1/vehicles/{id}")
    suspend fun getStarshipById(@Path("id") id: String): StarshipBasic
}