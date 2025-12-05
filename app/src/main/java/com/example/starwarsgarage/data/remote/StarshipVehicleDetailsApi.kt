package com.example.starwarsgarage.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface StarshipVehicleDetailsApi {
    @GET("starships/{id}")
    suspend fun getStarshipVehicleDetailsById(@Path("id") id: String): StarshipDetails
}
