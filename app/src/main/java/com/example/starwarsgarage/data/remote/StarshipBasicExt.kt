package com.example.starwarsgarage.data.remote

import com.example.starwarsgarage.data.local.StarshipItemEntity

fun StarshipBasic.toEntity(page: Int) = StarshipItemEntity(
    id = id,
    name = name,
    page = page,
    description = description,
    image = image
)
