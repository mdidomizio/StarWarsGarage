package com.example.starwarsgarage.domain.repository

import androidx.paging.PagingData
import com.example.starwarsgarage.domain.model.Starship
import kotlinx.coroutines.flow.Flow

interface StarshipRepository {
    fun getStarshipsStream(): Flow<PagingData<Starship>>
    suspend fun getStarshipProduct(id: String): Result<Starship>
    fun getAllStarships(): Flow<List<Starship>>
}
