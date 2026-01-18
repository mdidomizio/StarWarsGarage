package com.example.starwarsgarage.domain.model

import com.squareup.moshi.Json

data class Starship(
    @property:Json(name = "_id") val id: String,
    override val name: String?,
    val model: String?,
    val description: String? = null,
    override val image: String? = null,
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
    val url: String?,
    val isPdpLoaded: Boolean = false,
    val isFavourite: Boolean = false
) : GarageItem
