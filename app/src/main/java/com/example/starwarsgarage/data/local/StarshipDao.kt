package com.example.starwarsgarage.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StarshipDao {
    @Query("SELECT * FROM starship_items WHERE name LIKE '%' || :query || '%' ORDER BY name ASC ")
    fun pagingSearchItems(query: String): PagingSource<Int, StarshipItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(itemEntity: List<StarshipItemEntity>)

    @Query("DELETE FROM starship_items")
    suspend fun clearAll()
}
