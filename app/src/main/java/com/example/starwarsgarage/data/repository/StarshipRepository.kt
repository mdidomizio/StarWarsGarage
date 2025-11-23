package com.example.starwarsgarage.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipPagingSource
import kotlinx.coroutines.flow.Flow

interface StarshipRepository {
    fun getStarshipsStream(): Flow<PagingData<Starship>>
    suspend fun getStarshipById(id: String): Starship
}

class StarshipRepositoryImpl(
    private val starshipApi: StarshipApi
) : StarshipRepository {

    override fun getStarshipsStream(): Flow<PagingData<Starship>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { StarshipPagingSource(starshipApi) }
        ).flow
    }

    override suspend fun getStarshipById(id: String): Starship {
        return starshipApi.getStarshipById(id)
    }
}
