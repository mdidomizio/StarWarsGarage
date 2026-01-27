package com.example.starwarsgarage.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starwarsgarage.data.local.entity.StarshipItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StarshipDao {
    // for pagination
    @Query("SELECT * FROM starship_items ORDER BY name ASC")
    fun pagingSource(): PagingSource<Int, StarshipItemEntity>

    // for search
    @Query("SELECT * FROM starship_items WHERE name LIKE '%' || :query || '%' ORDER BY name ASC ")
    fun searchByName(query: String): Flow<List<StarshipItemEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(items: List<StarshipItemEntity>)

    @Query("DELETE FROM starship_items")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM starship_items")
    suspend fun getStarshipItemCount(): Int
}