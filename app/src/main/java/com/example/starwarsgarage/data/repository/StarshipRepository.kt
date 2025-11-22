package com.example.starwarsgarage.data.repository

import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipResponse

interface StarshipRepository {
    suspend fun getStarships(page: Int): StarshipResponse
    suspend fun getStarshipById(id: String): Starship
}

class StarshipRepositoryImpl(
    private val starshipApi: StarshipApi
) : StarshipRepository {

    override suspend fun getStarships(page: Int): StarshipResponse {
        return starshipApi.getStarships(page)
    }

    override suspend fun getStarshipById(id: String): Starship {
        return starshipApi.getStarshipById(id)
    }
}
