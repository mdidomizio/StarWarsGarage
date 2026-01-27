package com.example.starwarsgarage.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.starwarsgarage.data.local.StarWarsDatabase
import com.example.starwarsgarage.data.local.entity.RemoteKeysEntity
import com.example.starwarsgarage.data.local.entity.StarshipItemEntity
import com.example.starwarsgarage.data.remote.api.StarshipApi
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class StarshipItemRemoteMediator (
    private val api: StarshipApi,
    private val database: StarWarsDatabase
) : RemoteMediator<Int, StarshipItemEntity>() {

    private val starshipDao = database.starshipDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StarshipItemEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForlastItem(state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextKey
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevKey
                }
            }

            val response = api.getStarships(
                page = page,
                limit = state.config.pageSize
            )

            val starships = response.data.map { it.toEntity() }
            val endOfPaginationReached = response.info.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    starshipDao.clearAll()
                }
                val prevKey = if (page == 1) null else -1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = starships.map { starship ->
                    RemoteKeysEntity(
                        starshipItemId = starship.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                remoteKeysDao.insertAll(keys)
                starshipDao.insertAll(starships)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
