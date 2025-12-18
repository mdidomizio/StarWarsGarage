package com.example.starwarsgarage.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.starwarsgarage.data.local.FavoritesDataStore
import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipBasic
import com.example.starwarsgarage.data.remote.StarshipDetails
import com.example.starwarsgarage.data.remote.StarshipPagingSource
import com.example.starwarsgarage.data.remote.StarshipVehicleDetailsApi
import com.example.starwarsgarage.domain.model.Starship
import com.example.starwarsgarage.domain.repository.StarshipRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// key = name from the basic API, value = ID from swapi.dev
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
    "Trade Federation Battleship" to 32
)

class StarshipRepositoryImpl @Inject constructor(
    private val starshipApi: StarshipApi,
    private val starshipVehicleDetailsApi: StarshipVehicleDetailsApi,
    private val favoritesDataStore: FavoritesDataStore
) : StarshipRepository {

    override fun getStarshipsStream(): Flow<PagingData<Starship>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { StarshipPagingSource(starshipApi) }
        ).flow
        return combine(pager, favoritesDataStore.favoriteStarshipIds) {
            pagingData, favoriteId ->
            pagingData.map { starshipBasic ->
                starshipBasic.toStarship(
                    details = null,
                    isFavorite = favoriteId.contains(starshipBasic.id)
                )
            }
        }
    }

    override suspend fun getStarshipProduct(id: String): Result<Starship> = runCatching {
        val baseStarship = starshipApi.getStarshipById(id)
        val vehicleDetails = baseStarship.name?.let { STARSHIP_ID_MAP[it] }?.let {
            runCatching {
                starshipVehicleDetailsApi.getStarshipVehicleDetailsById(it.toString())
            }.getOrNull()
        }
        baseStarship.toStarship(vehicleDetails)
    }

    override fun getAllStarships(): Flow<List<Starship>> = flow {
         try {
            val allStarships = mutableListOf<StarshipBasic>()
            var page = 1
            var hasMorePages = true

            while (hasMorePages) {
                val response = starshipApi.getStarships(page = page)
                val fetchedItems = response.data

                allStarships.addAll(fetchedItems)
                if (response.info.next != null) page++ else hasMorePages = false
            }
            allStarships.map { starshipBasic ->
                starshipBasic.toStarship(null)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

private fun StarshipBasic.toStarship(
    details: StarshipDetails?,
    isFavorite: Boolean = false
) = Starship(
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
    isPdpLoaded = details != null,
    isFavorite = isFavorite
)
