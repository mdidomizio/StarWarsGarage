package com.example.starwarsgarage.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface StarshipVehicleDetailsApi {
    @GET("api/v1/vehicles/name/{vehicleName}")
    suspend fun getStarshipVehicleDetailsByName(@Path("vehicleName") vehicleName: String): StarshipVehicleDetails
}
