package com.example.starwarsgarage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.starwarsgarage.data.local.dao.FavoriteStarshipDao
import com.example.starwarsgarage.data.local.dao.RemoteKeysDao
import com.example.starwarsgarage.data.local.dao.StarshipDao
import com.example.starwarsgarage.data.local.entity.FavoriteStarshipEntity
import com.example.starwarsgarage.data.local.entity.RemoteKeysEntity
import com.example.starwarsgarage.data.local.entity.StarshipItemEntity

@Database(
    entities = [
        StarshipItemEntity::class,
        FavoriteStarshipEntity::class,
        RemoteKeysEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class StarWarsDatabase : RoomDatabase() {
    abstract fun favoriteStarshipDao(): FavoriteStarshipDao
    abstract fun starshipDao(): StarshipDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
