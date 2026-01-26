package com.example.starwarsgarage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        StarshipItemEntity::class,
        FavoriteStarshipEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class StarWarsDatabase : RoomDatabase() {
    abstract fun favoriteStarshipDao(): FavoriteStarshipDao
    abstract fun starshipDao(): StarshipDao
}
