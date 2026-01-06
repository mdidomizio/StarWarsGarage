package com.example.starwarsgarage.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_starships")
data class FavoriteStarship(
    @PrimaryKey val id: String
)
