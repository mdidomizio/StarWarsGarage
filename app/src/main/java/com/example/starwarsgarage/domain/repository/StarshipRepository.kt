package com.example.starwarsgarage.domain.repository

import androidx.paging.PagingData
import com.example.starwarsgarage.domain.model.Starship
import kotlinx.coroutines.flow.Flow

interface StarshipRepository {
    fun getStarshipsStream(query: String?): Flow<PagingData<Starship>>
    suspend fun getStarshipDetailsById(id: String): Result<Starship>
    fun getStarshipsByIds(ids: Set<String>): Flow<List<Starship>>
    suspend fun getRandomStarship(): Result<Starship?>
}
