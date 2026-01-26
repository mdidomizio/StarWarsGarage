package com.example.starwarsgarage.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.starwarsgarage.data.local.StarWarsDatabase
import com.example.starwarsgarage.data.local.StarshipItemEntity
import com.example.starwarsgarage.data.remote.api.StarshipApi

@OptIn(ExperimentalPagingApi::class)
class StarshipItemRemoteMediator (
    private val api: StarshipApi,
    private val database: StarWarsDatabase
) : RemoteMediator<Int, StarshipItemEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StarshipItemEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(true)
                lastItem.page + 1
            }

            LoadType.PREPEND -> return MediatorResult.Success(true)
        }
        return try {
            val response = api.getStarships(page)

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.starshipDao().clearAll()
                }
                database.starshipDao().insertAll(response.data.map { it.toEntity(page) })
            }
            MediatorResult.Success(
                endOfPaginationReached = response.data.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
