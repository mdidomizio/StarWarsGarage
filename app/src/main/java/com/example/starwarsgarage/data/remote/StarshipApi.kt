package com.example.starwarsgarage.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StarshipApi {
    @GET("api/v1/vehicles")
    suspend fun getStarships(@Query("page") page: Int): StarshipResponse

    @GET("api/v1/vehicles/{id}")
    suspend fun getStarshipById(@Path("id") id: String): StarshipBasic
}
