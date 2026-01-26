package com.example.starwarsgarage.data.remote.api

import com.example.starwarsgarage.data.remote.StarshipDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface StarshipVehicleDetailsApi {
    @GET("starships/{id}")
    suspend fun getStarshipVehicleDetailsById(@Path("id") id: String): StarshipDetails
}