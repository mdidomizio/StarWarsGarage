package com.example.starwarsgarage.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipBasic
import com.example.starwarsgarage.data.remote.StarshipDetails
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
            val vehicleDetails = baseStarship.name?.let { name ->
                try {
                    val detailsList = starshipVehicleDetailsApi.getStarshipVehicleDetailsByName(name)
                    Timber.tag("miriam").d("Second API call starship name for details: $name")
                    detailsList.firstOrNull()
                } catch (e: Exception) {
                    Timber.tag("miriam").e(e, "Failed to get vehicle details for $name")
                    null
                }
            }

            Timber.tag("miriam").d("vehicleDetails name: ${vehicleDetails?.name}")

            val starship = Starship(
                id = baseStarship.id,
                name = baseStarship.name,
                model = vehicleDetails?.model,
                manufacturer = vehicleDetails?.manufacturer,
                costInCredits = vehicleDetails?.costInCredits,
                length = vehicleDetails?.length,
                maxAtmospheringSpeed = vehicleDetails?.maxAtmospheringSpeed,
                crew = vehicleDetails?.crew,
                passengers = vehicleDetails?.passengers,
                cargoCapacity = vehicleDetails?.cargoCapacity,
                consumables = vehicleDetails?.consumables,
                hyperdriveRating = vehicleDetails?.hyperdriveRating,
                mglt = vehicleDetails?.mglt,
                starshipClass = vehicleDetails?.starshipClass,
                pilots = vehicleDetails?.pilots,
                films = vehicleDetails?.films,
                url = vehicleDetails?.url,
                description = baseStarship?.description,
                image = baseStarship?.image,
                isPdpLoaded = true
            )
            Result.success(starship)
        } catch (e: Exception) {
            Timber.tag("miriam").e(e, "Failed to get starship product for id $id")
            Result.failure(e)
        }
    }
}
