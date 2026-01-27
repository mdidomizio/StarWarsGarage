package com.example.starwarsgarage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_starships")
data class FavoriteStarshipEntity(
    @PrimaryKey val id: String
)