package com.example.starwarsgarage.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "starship_items")
data class StarshipItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val page: Int,
    val description: String,
    val image: String
)
