package com.example.starwarsgarage.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipBasic
import com.example.starwarsgarage.data.remote.StarshipPagingSource
import com.example.starwarsgarage.data.remote.StarshipVehicleDetailsApi
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

interface StarshipRepository {
    fun getStarshipsStream(): Flow<PagingData<StarshipBasic>>
    suspend fun getStarshipProduct(id: String): Result<Starship>
}

class StarshipRepositoryImpl @Inject constructor(
    private val starshipApi: StarshipApi,
    private val starshipVehicleDetailsApi: StarshipVehicleDetailsApi
) : StarshipRepository {

    override fun getStarshipsStream(): Flow<PagingData<StarshipBasic>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { StarshipPagingSource(starshipApi) }
        ).flow
    }

    override suspend fun getStarshipProduct(id: String): Result<Starship> {
        return try {
            val baseStarship = starshipApi.getStarshipById(id)
            Timber.tag("miriam").d("First API call starship name: ${baseStarship.name}")
            val vehicleDetails = try {
                val detailsList = starshipVehicleDetailsApi.getStarshipVehicleDetailsByName(baseStarship.name)
                Timber.tag("miriam").d("Second API call starship name for details: ${baseStarship.name}")
                detailsList.firstOrNull()
            } catch (e: Exception) {
                Timber.tag("miriam").e(e, "Failed to get vehicle details for ${baseStarship.name}")
                null
            }

            val product = Starship(
                id = id, // Use the id passed to the function
                name = baseStarship.name,
                model = baseStarship.model,
                manufacturer = baseStarship.manufacturer,
                costInCredits = baseStarship.costInCredits,
                length = baseStarship.length,
                maxAtmospheringSpeed = baseStarship.maxAtmospheringSpeed,
                crew = baseStarship.crew,
                passengers = baseStarship.passengers,
                cargoCapacity = baseStarship.cargoCapacity,
                consumables = baseStarship.consumables,
                hyperdriveRating = baseStarship.hyperdriveRating,
                mglt = baseStarship.mglt,
                starshipClass = baseStarship.starshipClass,
                pilots = baseStarship.pilots,
                films = baseStarship.films,
                url = baseStarship.url,
                description = vehicleDetails?.description,
                image = vehicleDetails?.image,
                isPdpLoaded = true // Mark that we have the detailed data
            )
            Result.success(product)
        } catch (e: Exception) {
            Timber.tag("miriam").e(e, "Failed to get starship product for id $id")
            Result.failure(e)
        }
    }
}
