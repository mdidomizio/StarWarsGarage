package com.example.starwarsgarage.data.remote

import com.squareup.moshi.Json

data class StarshipBasic(
    @Json(name = "_id") val id: String,
    val name: String,
    val description: String,
    val image: String
)
