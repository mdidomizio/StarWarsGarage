package com.example.starwarsgarage.data.repository

import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.data.remote.StarshipApi

interface StarshipRepository {
    suspend fun getStarships(): List<Starship>
    suspend fun getStarshipById(id: String): Starship
}

class StarshipRepositoryImpl(
    private val starshipApi: StarshipApi
) : StarshipRepository {

    override suspend fun getStarships(): List<Starship> {
        return starshipApi.getStarships().results
    }

    override suspend fun getStarshipById(id: String): Starship {
        return starshipApi.getStarshipById(id)
    }
}
