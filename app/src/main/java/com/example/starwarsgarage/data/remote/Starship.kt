package com.example.starwarsgarage.data.remote

import com.squareup.moshi.Json

// domain model for ui
data class Starship(
    val id: String,
    val name: String,
    val model: String,
    val description: String? = null,
    val image: String? = null,
    val manufacturer: String,
    @Json(name = "cost_in_credits") val costInCredits: String,
    val length: String,
    @Json(name = "max_atmosphering_speed") val maxAtmospheringSpeed: String,
    val crew: String,
    val passengers: String,
    @Json(name = "cargo_capacity") val cargoCapacity: String,
    val consumables: String,
    @Json(name = "hyperdrive_rating") val hyperdriveRating: String,
    @Json(name = "MGLT") val mglt: String,
    @Json(name = "starship_class") val starshipClass: String,
    val pilots: List<String>,
    val films: List<String>,
    val url: String,
    val isPdpLoaded: Boolean = false
)

// first api response
data class StarshipBasic(
    val name: String,
    val model: String,
    val manufacturer: String,
    @Json(name = "cost_in_credits") val costInCredits: String,
    val length: String,
    @Json(name = "max_atmosphering_speed") val maxAtmospheringSpeed: String,
    val crew: String,
    val passengers: String,
    @Json(name = "cargo_capacity") val cargoCapacity: String,
    val consumables: String,
    @Json(name = "hyperdrive_rating") val hyperdriveRating: String,
    @Json(name = "MGLT") val mglt: String,
    @Json(name = "starship_class") val starshipClass: String,
    val pilots: List<String>,
    val films: List<String>,
    val url: String
) {
    val id: String
        get() = url.split("/").dropLast(1).last()
}
