package com.example.starwarsgarage.data.repository

import android.content.Context
import androidx.core.content.edit
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipBasic
import com.example.starwarsgarage.data.remote.StarshipDetails
import com.example.starwarsgarage.data.remote.StarshipPagingSource
import com.example.starwarsgarage.data.remote.StarshipVehicleDetailsApi
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

// key = name from the basic API, value = ID from swapi.info
private val STARSHIP_ID_MAP = hashMapOf(
    "Imperial Star Destroyer" to 3,
    "Millennium Falcon" to 10,
    "Imperial Shuttle" to 22,
    "Resistance X-Wing" to 12,
    "Resistance Y-wing starfighter" to 11,
    "Y-wing Starfighter" to 11,
    "X-wing Starfighter" to 12,
    "Slave I" to 21,
    "Executor" to 15,
    "A-wing Fighter" to 28,
    "B-wing Fighter" to 29,
    "Republic Cruiser" to 31,
    "Naboo N-1 Starfighter" to 39,
    "Naboo Royal Starship" to 40,
    "Nebulon-B Frigate" to 23,
    "Mon Calamari Star Cruiser" to 27,
    "GR-75 Medium Transport" to 17,
    "Tantive IV" to 2,
    "Darth Vader's TIE Fighter" to 13,
    "TIE Fighter" to 13,
    "TIE Interceptor" to 13,
    "Trade Federation Battleship" to 32,
    "AAT Battle Tank" to 4
)

@Singleton
class StarshipRepositoryImpl @Inject constructor(
    private val starshipApi: StarshipApi,
    private val starshipVehicleDetailsApi: StarshipVehicleDetailsApi
) : StarshipRepository {

    override fun getStarshipsStream(): Flow<PagingData<Starship>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { StarshipPagingSource(starshipApi) }
        ).flow.map { pagingData ->
            pagingData.map { starshipBasic ->
                starshipBasic.toStarship(null)
            }
        }
    }

    override suspend fun getStarshipDetailsById(id: String): Result<Starship> {
        try {
            val baseStarship = starshipApi.getStarshipById(id)

            val vehicleDetailsId = STARSHIP_ID_MAP[baseStarship.name]
            if (vehicleDetailsId == null) {
                return Result.success(baseStarship.toStarship(null))
            }
            return try {
                val vehicleDetails = starshipVehicleDetailsApi.getStarshipVehicleDetailsById(vehicleDetailsId.toString())
                Result.success(baseStarship.toStarship(vehicleDetails))
            } catch (e: Exception) {
                Result.success(baseStarship.toStarship(null))
            }

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}

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
