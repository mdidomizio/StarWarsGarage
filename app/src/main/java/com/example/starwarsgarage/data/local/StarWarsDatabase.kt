package com.example.starwarsgarage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteStarship::class],
    version = 1,
    exportSchema = false
)
abstract class StarWarsDatabase : RoomDatabase() {
    abstract fun favoriteStarshipDao(): FavoriteStarshipDao
}
