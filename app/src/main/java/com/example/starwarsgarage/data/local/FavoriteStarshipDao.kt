package com.example.starwarsgarage.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteStarshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteStarshipEntity)

    @Delete
    suspend fun delete(favorite: FavoriteStarshipEntity)

    @Query("SELECT * FROM favorite_starships WHERE id = :starshipId")
    suspend fun getFavoriteById(starshipId: String): FavoriteStarshipEntity?

    @Query("SELECT id FROM favorite_starships")
    fun getFavoriteIds(): Flow<List<String>>
}
