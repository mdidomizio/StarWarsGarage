package com.example.starwarsgarage.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.example.starwarsgarage.data.local.dao.RemoteKeysDao
import com.example.starwarsgarage.data.local.StarWarsDatabase
import com.example.starwarsgarage.data.local.dao.StarshipDao
import com.example.starwarsgarage.data.local.entity.StarshipItemEntity
import com.example.starwarsgarage.data.remote.api.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipBasic
import com.example.starwarsgarage.data.remote.StarshipDetails
import com.example.starwarsgarage.data.remote.StarshipItemRemoteMediator
import com.example.starwarsgarage.data.remote.api.StarshipVehicleDetailsApi
import com.example.starwarsgarage.data.remote.toEntity
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import com.example.starwarsgarage.domain.model.STARSHIP_ID_MAP
import kotlinx.coroutines.flow.flowOf

@Singleton
class StarshipRepositoryImpl @Inject constructor(
    private val starshipApi: StarshipApi,
    private val starshipVehicleDetailsApi: StarshipVehicleDetailsApi,
    private val database: StarWarsDatabase
) : StarshipRepository {
    private val starshipDao = database.starshipDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getStarshipsStream(): Flow<PagingData<Starship>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            remoteMediator = StarshipItemRemoteMediator(
                api = starshipApi,
                database = database
            ),
            pagingSourceFactory = { starshipDao.pagingSource() }
        ).flow
            /*.map { pagingData ->
            pagingData.map { starshipItemEntity ->
                starshipItemEntity.toStarshipBasic().toStarship(null)
            }
        }*/
    }
    suspend fun syncAllStarshipItemsSequential(): Result<Unit> {
        return try {
            val allStarshipItems = mutableListOf<Starship>()
            var currentPage = 1
            val pageSize = 50
            val firstResponse = starshipApi.getStarships(
                page = currentPage,
                limit = pageSize
            )
            val totalPages = firstResponse.info.pages ?: 1
            val totalCount = firstResponse.info.count ?: 0

            Log.d("StarshipRepository", "Starting sync: $totalCount items across $totalPages pages")

            allStarshipItems.addAll(firstResponse.data.map { it.toEntity() } )
            currentPage++

            while (currentPage <= totalPages) {
                val response = starshipApi.getStarships(
                    page = currentPage,
                    limit = pageSize
                )
                val starships = response.data.map { it.toEntity() }
                allStarshipItems.addAll(starships)
                Log.d("StarshipRepository", "Fetched page $currentPage/$totalPages (${allStarshipItems.size}/$totalCount items)")
                currentPage++

                if (currentPage> 100) {
                    Log.w("StarshipRepository", "Reached safety limit at page 100")
                    break
                }
            }

            database.withTransaction {
                starshipDao.clearAll()
                starshipDao.insertAll(allStarshipItems)
            }

            Log.d("StarshipRepository", "Sync complete: ${allStarshipItems.size} starships saved")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.d("StarshipRepository", "Sync complete: ${allStarshipItems.size} starships saved")
            Result.failure(e)
        }
    }

    fun searchStarshipItems(query: String): Flow<List<Starship>> {
        return (if (query.isEmpty()) {
            flowOf(emptyList())
        } else {
            starshipDao.searchByName("%$query%")
        }) as Flow<List<Starship>>
    }

    override suspend fun getStarshipDetailsById(id: String): Result<Starship> {
        return try {
            val baseStarship = starshipApi.getStarshipById(id)

            val vehicleDetailsId = STARSHIP_ID_MAP[baseStarship.name] ?: return Result.success(
                baseStarship.toStarship(null)
            )
            try {
                val vehicleDetails =
                    starshipVehicleDetailsApi.getStarshipVehicleDetailsById(vehicleDetailsId.toString())
                Result.success(baseStarship.toStarship(vehicleDetails))
            } catch (e: Exception) {
                Timber.e(e, "Failed to get vehicle details for starship: ${baseStarship.name}")
                Result.success(baseStarship.toStarship(null))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getStarshipsByIds(ids: Set<String>): Flow<List<Starship>> =
        flow {
            if (ids.isEmpty()) {
                emit(emptyList())
                return@flow
            }
            val starship = coroutineScope {
                ids.map { id ->
                    async { getStarshipDetailsById(id) }
                }.mapNotNull { deferredResult ->
                    deferredResult.await().getOrNull()
                }
            }
            emit(starship)
        }

    override suspend fun getRandomStarship(): Result<Starship?> {
        return try {
            val randomPage = (1..267).random()
            val response =  starshipApi.getStarships(page = randomPage, limit = 1)
            val starship =  response.data.firstOrNull()?.toStarship(null)
            Result.success(starship)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

private fun StarshipItemEntity.toStarshipBasic() = StarshipBasic(
    id = id,
    name = name,
    description = description,
    image = image
)

private fun StarshipBasic.toStarship(details: StarshipDetails?) = Starship(
    id = id,
    name = name,
    description = description,
    image = image,
    model = details?.model,
    manufacturer = details?.manufacturer,
    costInCredits = details?.costInCredits,
    length = details?.length,
    maxAtmospheringSpeed = details?.maxAtmospheringSpeed,
    crew = details?.crew,
    passengers = details?.passengers,
    cargoCapacity = details?.cargoCapacity,
    consumables = details?.consumables,
    hyperdriveRating = details?.hyperdriveRating,
    mglt = details?.mglt,
    starshipClass = details?.starshipClass,
    pilots = details?.pilots,
    films = details?.films,
    url = details?.url,
    isPdpLoaded = details != null
)
