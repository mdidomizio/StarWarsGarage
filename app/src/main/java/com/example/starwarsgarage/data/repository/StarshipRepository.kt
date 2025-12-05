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

// A map where the key is the name from the basic API and the value is the ID from swapi.dev
private val STARSHIP_ID_MAP = mapOf(
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
    "Trade Federation Battleship" to 32
)

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

                    val vehicleId = STARSHIP_ID_MAP[name]
                    val details = vehicleId?.let {
                        starshipVehicleDetailsApi.getStarshipVehicleDetailsById(it.toString())
                    }
                    details

                    /*// if name exists in keys of hashmap, do call to get more data
                    val hashMapOfShipIDs = HashMap<String, Int>()
                    // Name is from Large API, id is from small API, corresponding
                    hashMapOfShipIDs["Imperial Star Destroyer"] = 3

                    val id = hashMapOfShipIDs["Imperial Star Destroyer"]
                    // NEED ID HERE - eg "4"

                    val details = starshipVehicleDetailsApi.getStarshipVehicleDetailsByName(*//*id*//*)
                    Timber.tag("miriam").d("Second API call starship name for details: $name")
                    details*/
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
