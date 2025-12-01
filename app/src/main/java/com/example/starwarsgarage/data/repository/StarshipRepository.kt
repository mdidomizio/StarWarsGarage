package com.example.starwarsgarage.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwarsgarage.data.remote.Starship
import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipVehicleDetailsApi
import com.example.starwarsgarage.data.remote.StarshipPagingSource
import com.example.starwarsgarage.data.remote.StarshipProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StarshipRepository {
    fun getStarshipsStream(): Flow<PagingData<Starship>>
    suspend fun getStarshipProduct(id: String): Result<StarshipProduct>
}

class StarshipRepositoryImpl @Inject constructor(
    private val starshipApi: StarshipApi,
    private val starshipVehicleDetailsApi: StarshipVehicleDetailsApi
) : StarshipRepository {

    override fun getStarshipsStream(): Flow<PagingData<Starship>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { StarshipPagingSource(starshipApi) }
        ).flow
    }

    override suspend fun getStarshipProduct(id: String): Result<StarshipProduct> {
        return try {
            val baseStarship = starshipApi.getStarshipById(id)
            val vehicleDetails = try {
                starshipVehicleDetailsApi.getStarshipVehicleDetailsByName(baseStarship.name)
            } catch (e: Exception) {
                null
            }

            val product = StarshipProduct(
                id = baseStarship.id,
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
            Result.failure(e)
        }
    }
}
