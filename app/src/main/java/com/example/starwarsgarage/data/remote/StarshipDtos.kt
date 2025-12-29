package com.example.starwarsgarage.data.remote

import com.squareup.moshi.Json

// Data Transfer Object (DTO) design pattern

// BASE_URL_STARSHIP = https://starwars-databank-server.vercel.app/
data class StarshipBasic(
    @property:Json(name = "_id") val id: String,
    val name: String,
    val description: String,
    val image: String
)


// BASE_URL_STARSHIP_VEHICLE_DETAILS = https://swapi.info/api/
data class StarshipDetails(
    val name: String?,
    val model: String?,
    val manufacturer: String?,
    @property:Json(name = "cost_in_credits") val costInCredits: String?,
    val length: String?,
    @property:Json(name = "max_atmosphering_speed") val maxAtmospheringSpeed: String?,
    val crew: String?,
    val passengers: String?,
    @property:Json(name = "cargo_capacity") val cargoCapacity: String?,
    val consumables: String?,
    @property:Json(name = "hyperdrive_rating") val hyperdriveRating: String?,
    @property:Json(name = "MGLT") val mglt: String?,
    @property:Json(name = "starship_class") val starshipClass: String?,
    val pilots: List<String>?,
    val films: List<String>?,
    val url: String?
) {
    val id: String?
        get() = url?.trimEnd('/')?.substringAfterLast('/')
}

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
